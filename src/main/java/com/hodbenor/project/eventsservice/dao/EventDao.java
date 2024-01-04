package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    long insertEvent(Event event);
    List<Event> findAllEvents();
    List<Event> findAllEvents(LocalDateTime fromDate, LocalDateTime toDate);
    Optional<Event> findById(long eventId);
    List<Event> findEventsByVenue(String venue);
    List<Event> findEventsByLocation(String location);
    List<Event> findSortedEvents(String orderBy);
    boolean updateEvent(Event event);
    Optional<Event> removeEvent(long eventId);

}
