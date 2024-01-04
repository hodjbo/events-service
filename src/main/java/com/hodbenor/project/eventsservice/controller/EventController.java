package com.hodbenor.project.eventsservice.controller;

import com.hodbenor.project.eventsservice.controller.beans.EventUserRequest;
import com.hodbenor.project.eventsservice.dao.beans.Event;
import com.hodbenor.project.eventsservice.service.EventService;
import com.hodbenor.project.eventsservice.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Event> insertEvent(@RequestBody Event event) {

        long eventId = eventService.insertEvent(event);

        return eventId > 0 ? ResponseEntity.ok(event) : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody EventUserRequest eventUserRequest) {
        AtomicBoolean isSignedToEvent = new AtomicBoolean(false);
        userService.findUserById(eventUserRequest.getUserId()).ifPresent(user -> {
            isSignedToEvent.set(eventService.signupToEvent(eventUserRequest.getEventId(), user.getId()));
        });

        return isSignedToEvent.get() ? ResponseEntity.ok(isSignedToEvent.get()) : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable long eventId) {

        return eventService.getEvent(eventId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping()
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {

        eventService.updateEvent(event);

        return eventService.updateEvent(event) ? ResponseEntity.ok(event) : ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> removeEvent(@PathVariable long eventId) {

        return eventService.removeEvent(eventId) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/scheduled-events")
    public ResponseEntity<List<Event>> getScheduledEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/scheduled-events/date")
    public ResponseEntity<List<Event>> getScheduledEvents(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        return ResponseEntity.ok(eventService.getAllEvents(fromDate, toDate));
    }

    @GetMapping("/scheduled-events/venue/{venue}")
    public ResponseEntity<List<Event>> getEventsByVenue(@PathVariable("venue") String venue) {

        return ResponseEntity.ok(eventService.getEventsByVenue(venue));
    }

    @GetMapping("/scheduled-events/location/{location}")
    public ResponseEntity<List<Event>> getEventsByLocation(@PathVariable("location") String location) {

        return ResponseEntity.ok(eventService.getEventsByLocation(location));
    }

    @GetMapping("/scheduled-events/{sorted}")
    public ResponseEntity<List<Event>> getEventsSortedByDate(@PathParam("sorted") String orderBy) {

        return ResponseEntity.ok(eventService.getSortedEvents(orderBy));
    }

}
