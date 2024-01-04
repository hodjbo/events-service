package com.hodbenor.project.eventsservice.controller.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventUserRequest implements Serializable {
    private final long eventId;
    private final long userId;
}
