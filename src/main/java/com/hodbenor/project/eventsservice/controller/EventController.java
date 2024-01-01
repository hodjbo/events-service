package com.hodbenor.project.eventsservice.controller;

import com.hodbenor.project.eventsservice.dao.beans.Event;
import com.hodbenor.project.eventsservice.service.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger LOGGER = LogManager.getLogger(EventController.class);
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllScheduledEvents(
            /*@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate*/) {

        List<Event> events = eventService.getAllScheduledEvents(null, null);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events2")
    public ResponseEntity<Event> insertEvent() {

        eventService.insertEvent();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable long eventId) {

        return eventService.getEvent(eventId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable long eventId, @RequestBody Event event) {

        eventService.updateEvent(event);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> removeEvent(@PathVariable long eventId) {

        eventService.removeEvent(eventId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
