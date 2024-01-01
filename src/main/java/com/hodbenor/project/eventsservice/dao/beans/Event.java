package com.hodbenor.project.eventsservice.dao.beans;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "VENUE", length = 20, nullable = false)
    private String venue;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "MAX_PARTICIPANTS", nullable = false)
    private int maxParticipants;

    @Column(name = "NUM_PARTICIPANTS")
    private int numParticipants;

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "LOCATION", length = 40, nullable = false)
    private String location;
}
