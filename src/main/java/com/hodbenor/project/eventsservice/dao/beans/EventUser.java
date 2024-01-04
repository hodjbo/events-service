package com.hodbenor.project.eventsservice.dao.beans;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event_user")
@Data
public class EventUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;
}