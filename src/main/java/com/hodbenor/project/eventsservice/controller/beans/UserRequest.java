package com.hodbenor.project.eventsservice.controller.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequest implements Serializable {
    private final String username;
    private final String password;
}
