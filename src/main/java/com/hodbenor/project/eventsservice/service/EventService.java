package com.hodbenor.project.eventsservice.service;

import com.hodbenor.project.eventsservice.dao.EventDao;
import com.hodbenor.project.eventsservice.dao.EventUserDao;
import com.hodbenor.project.eventsservice.dao.beans.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EventService {
    private static final Logger LOGGER = LogManager.getLogger(EventService.class);

    private final EventReminderService eventReminderService;
    private final EventDao eventDao;
    private final EventUserDao eventUserDao;

    public EventService(EventReminderService eventReminderService, EventDao eventDao, EventUserDao eventUserDao) {
        this.eventReminderService = eventReminderService;
        this.eventDao = eventDao;
        this.eventUserDao = eventUserDao;
    }

    public long insertEvent(Event event) {
        event.setCreationDateTime(LocalDateTime.now());
        try {

            long eventId = eventDao.insertEvent(event);
            eventReminderService.addEvent(event);

            return eventId;
        } catch (Exception e) {
            LOGGER.error("Failed to insert new event: {}", event, e);

            return -1;
        }
    }

    public boolean signupToEvent(long eventId, long userId) {
        AtomicBoolean isSignedToEvent = new AtomicBoolean(false);
        try {
            eventDao.findById(eventId).ifPresent(event -> {
                if (event.getDateTime().isAfter(LocalDateTime.now())) {
                    eventUserDao.signupToEvent(eventId, userId);
                    isSignedToEvent.set(true);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Failed to signup user {} for event : {}", userId, eventId, e);
            isSignedToEvent.set(false);

        }

        return isSignedToEvent.get();
    }


    public List<Event> getAllEvents(LocalDateTime fromDate, LocalDateTime toDate) {

        return eventDao.findAllEvents(fromDate, toDate);
    }

    public List<Event> getAllEvents() {

        return eventDao.findAllEvents();
    }

    public List<Event> getEventsByVenue(String venue) {

        return eventDao.findEventsByVenue(venue);
    }

    public List<Event> getEventsByLocation(String location) {

        return eventDao.findEventsByLocation(location);
    }

    public List<Event> getSortedEvents(String orderBy) {

        return switch (orderBy) {
            case "date" -> eventDao.findSortedEvents("dateTime");
            case "popularity" -> eventDao.findSortedEvents("numParticipants");
            case "creation-time" -> eventDao.findSortedEvents("creationDateTime");
            default -> throw new IllegalStateException("Unexpected value: " + orderBy);
        };
    }

    public Optional<Event> getEvent(long eventId) {

        return eventDao.findById(eventId);
    }

    public boolean updateEvent(Event event) {
        LOGGER.debug("try to update an event {}", event);

        return eventDao.updateEvent(event);
    }

    public boolean removeEvent(long eventId) {
        LOGGER.debug("try to remove an event {}", eventId);

        return eventDao.removeEvent(eventId)
                .map(eventReminderService::removeEvent)
                .orElse(false);
    }
}