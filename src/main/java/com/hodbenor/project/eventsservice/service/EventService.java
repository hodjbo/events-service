package com.hodbenor.project.eventsservice.service;

import com.hodbenor.project.eventsservice.dao.EventDao;
import com.hodbenor.project.eventsservice.dao.beans.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private static final Logger LOGGER = LogManager.getLogger(EventService.class);

    private final EventDao eventDao;

    public void insertEvent() {

        Event event = new Event();
        event.setDate(LocalDateTime.now());
        event.setCreationDate(LocalDateTime.now());
        event.setName("AAAA");
        event.setVenue("AAAA");
        event.setDescription("AAAA");
        event.setLocation("AAAA");
        event.setMaxParticipants(10);

        eventDao.insertEvent(event);
    }

    public EventService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public List<Event> getAllScheduledEvents(LocalDateTime fromDate, LocalDateTime toDate) {

        return eventDao.fetchAllScheduledEvents(fromDate, toDate);
    }

    public Optional<Event> getEvent(long eventId) {

        return eventDao.findById(eventId);
    }

    public void updateEvent(Event event) {

        eventDao.updateEvent(event);
    }

    public void removeEvent(long eventId) {

        eventDao.removeEvent(eventId);
    }
}