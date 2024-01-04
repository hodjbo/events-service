package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.EventUser;

import java.util.List;

public interface EventUserDao {
    void signupToEvent(long eventId, long userId);
    List<EventUser> findEventUser(long eventId);
}
