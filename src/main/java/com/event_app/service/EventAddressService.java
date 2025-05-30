package com.event_app.service;

import com.event_app.dto.EventAddressDto;
import com.event_app.entity.Event;
import com.event_app.entity.EventAddress;
import com.event_app.repository.EventAddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventAddressService {

  private final EventAddressRepository repository;

  public EventAddress createEventAddress(EventAddressDto dto, Event event) {
    log.info("Creating event address for event {}", event.getId());
    var address = EventAddress.builder()
        .name(dto.name())
        .city(dto.city())
        .street(dto.street())
        .postCode(dto.postCode())
        .event(event)
        .build();
    return repository.save(address);
  }

  public EventAddress updateEventAddress(EventAddressDto dto, Event event) {
    log.info("Updating event address for event {}", event.getId());

    var existingAddress = repository.findById(dto.id())
        .orElse(null);

    if (existingAddress != null) {
      existingAddress.setName(dto.name())
          .setCity(dto.city())
          .setStreet(dto.street())
          .setPostCode(dto.postCode());
      return repository.save(existingAddress);
    } else {
      return createEventAddress(dto, event);
    }
  }
}
