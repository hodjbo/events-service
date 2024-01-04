package com.hodbenor.project.eventsservice.controller;

import com.hodbenor.project.eventsservice.dao.beans.Event;
import com.hodbenor.project.eventsservice.dao.beans.User;
import com.hodbenor.project.eventsservice.service.EventService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<Event> insertEvent(@RequestBody Event event) {

        long eventId = eventService.insertEvent(event);

        return eventId > 0 ? ResponseEntity.ok(event) : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/signup/{eventId}")
    public ResponseEntity<Boolean> signUp(User user, @RequestHeader(name = "Auth-Token") String authToken, @PathVariable long eventId) {

        return  ResponseEntity.ok(eventService.signupToEvent(eventId, user.getId()));
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

    @GetMapping("/scheduled-events/venue/{venue}")
    public ResponseEntity<List<Event>> getEventsByVenue(User user, @RequestHeader(name = "Auth-Token") String authToken,
                                                        @PathVariable("venue") String venue) {

        return ResponseEntity.ok(eventService.getEventsByVenue(venue));
    }

    @GetMapping("/scheduled-events/location/{location}")
    public ResponseEntity<List<Event>> getEventsByLocation(User user, @RequestHeader(name = "Auth-Token") String authToken,
                                                           @PathVariable("location") String location) {

        return ResponseEntity.ok(eventService.getEventsByLocation(location));
    }

    @GetMapping("/scheduled-events/sorted")
    public ResponseEntity<List<Event>> getEventsSortedByDate(User user, @RequestHeader(name = "Auth-Token") String authToken,
                                                             @PathParam("orderBy") String orderBy) {

        return ResponseEntity.ok(eventService.getSortedEvents(orderBy));
    }

}
