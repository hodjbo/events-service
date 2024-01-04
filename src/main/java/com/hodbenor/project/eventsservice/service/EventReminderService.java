package com.hodbenor.project.eventsservice.service;

import com.hodbenor.project.eventsservice.dao.EventUserDao;
import com.hodbenor.project.eventsservice.dao.beans.Event;
import com.hodbenor.project.eventsservice.dao.beans.EventUser;
import com.hodbenor.project.eventsservice.dao.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class EventReminderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    private final EventUserDao eventUserDao;
    private final UserService userService;
    private final PriorityQueue<Event> eventQueue;
    private final Thread reminderThread;

    public EventReminderService(EventUserDao eventUserDao, UserService userService) {
        this.eventUserDao = eventUserDao;
        this.userService = userService;
        eventQueue = new PriorityQueue<>(Comparator.comparing(Event::getDateTime));
        reminderThread = new Thread(this::handleEvents);
        reminderThread.start();
    }

    public void addEvent(Event event) {
        synchronized (eventQueue) {
            eventQueue.add(event);
            eventQueue.notify();
        }
    }

    public boolean removeEvent(Event event) {
        synchronized (eventQueue) {
            return eventQueue.remove(event);
        }
    }

    private void handleEvents() {
        while (true) {
            synchronized (eventQueue) {
                while (eventQueue.isEmpty()) {
                    try {
                        eventQueue.wait();
                    } catch (InterruptedException e) {
                        LOGGER.error("Thread interrupted while waiting for events in the empty event queue", e);
                    }
                }

                long delay = 0;
                Event nextEvent;
                do {
                    nextEvent = eventQueue.peek();
                    if (nextEvent != null) {
                        Instant eventDateInstant = nextEvent.getDateTime().minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
                        Instant nowInstant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
                        Duration duration = Duration.between(nowInstant, eventDateInstant);
                        delay = duration.toMillis();
                    }

                    try {
                        eventQueue.wait(delay);
                    } catch (InterruptedException e) {
                        LOGGER.error("Thread interrupted while waiting to notify an event", e);
                    }
                    nextEvent = eventQueue.peek();
                    if (nextEvent != null) {
                        Instant eventDateInstant = nextEvent.getDateTime().minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
                        Instant nowInstant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
                        Duration duration = Duration.between(nowInstant, eventDateInstant);
                        delay = duration.toMillis();
                    }
                } while (delay > 0);

                nextEvent = eventQueue.poll();
                if (nextEvent != null) {
                    sendReminder(nextEvent);
                }
            }
        }
    }

    private void sendReminder(Event event) {
        LOGGER.debug("sendReminder of event {}", event);

        List<EventUser> eventUsers = eventUserDao.findEventUser(event.getId());
        eventUsers.forEach(eventUser -> {
            userService.findUserById(eventUser.getUserId())
                    .ifPresent(user -> notifyUser(user, event));
        });
    }

    private void notifyUser(User user, Event event) {
        LOGGER.debug("notifyUser to {} for {}", user, event.getDateTime());
    }

}
