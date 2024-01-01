package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    void insertEvent(Event event);
    List<Event> fetchAllScheduledEvents(LocalDateTime fromDate, LocalDateTime toDate);
    Optional<Event> findById(long event);
    void updateEvent(Event event);
    void removeEvent(long eventId);

}
