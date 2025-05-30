package com.event_app.service;

import com.event_app.dto.EventDto;
import com.event_app.entity.Event;
import com.event_app.mapper.EventMapper;
import com.event_app.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventService {

  private final EventRepository repository;
  private final EventAddressService eventAddressService;

  public List<EventDto> getAllEvents() {
    log.info("Getting all events");
    return repository.findAll().stream().map(EventMapper::toDto).toList();
  }

  public EventDto getEventById(Long id) {
    log.info("Getting event with id {}", id);
    return EventMapper.toDto(findEventById(id));
  }

  public EventDto createEvent(EventDto dto) {
    log.info("Creating event with name {}", dto.name());

    var event = Event.builder()
        .name(dto.name())
        .description(dto.description())
        .date(dto.date())
        .availableTickets(dto.availableTickets())
        .ticketPrice(dto.ticketPrice())
        .adult(dto.adult())
        .build();

    repository.save(event);

    var eventAddress = eventAddressService.createEventAddress(dto.address(), event);
    event.setAddress(eventAddress);
    return EventMapper.toDto(event);
  }

  public EventDto updateEvent(Long id, EventDto dto) {
    log.info("Updating event with id {}", id);
    var event = findEventById(id);

    event.setName(dto.name())
        .setDescription(dto.description())
        .setDate(dto.date())
        .setAvailableTickets(dto.availableTickets())
        .setTicketPrice(dto.ticketPrice())
        .setAdult(dto.adult());

    if (dto.address() != null) {
      if (dto.address().id() != null) {
        var eventAddress = eventAddressService.updateEventAddress(dto.address(), event);
        event.setAddress(eventAddress);
      } else {
        var eventAddress = eventAddressService.createEventAddress(dto.address(), event);
        event.setAddress(eventAddress);
      }
    }

    return EventMapper.toDto(repository.save(event));
  }

  public void deleteEvent(Long id) {
    log.info("Deleting event with id {}", id);
    repository.delete(findEventById(id));
  }

  public Event findEventById(Long id) {
    log.info("Finding event with id {}", id);
    return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));
  }
}