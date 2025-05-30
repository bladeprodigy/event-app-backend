package com.event_app.controller;

import com.event_app.dto.EventDto;
import com.event_app.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
class EventController {

  private final EventService eventService;

  @GetMapping
  ResponseEntity<List<EventDto>> getAllEvents() {
    var events = eventService.getAllEvents();
    return ResponseEntity.ok(events);
  }

  @GetMapping("/{id}")
  ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
    var event = eventService.getEventById(id);
    return ResponseEntity.ok(event);
  }

  @PostMapping
  ResponseEntity<EventDto> createEvent(@RequestBody @Valid EventDto dto) {
    var event = eventService.createEvent(dto);
    return ResponseEntity.status(201).body(event);
  }

  @PutMapping("/{id}")
  ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @Valid EventDto dto) {
    var updatedEvent = eventService.updateEvent(id, dto);
    return ResponseEntity.ok(updatedEvent);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
    eventService.deleteEvent(id);
    return ResponseEntity.noContent().build();
  }
}
