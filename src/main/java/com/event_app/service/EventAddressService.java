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
    log.info("Creating event address with name {} for event with id {}", dto.name(), event.getId());
    return repository.save(EventAddress.builder()
        .name(dto.name())
        .city(dto.city())
        .street(dto.street())
        .postCode(dto.postCode())
        .event(event)
        .build());
  }
}
