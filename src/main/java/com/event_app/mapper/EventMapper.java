package com.event_app.mapper;

import com.event_app.dto.EventDto;
import com.event_app.entity.Event;

public class EventMapper {

  public static EventDto toDto(final Event event) {
    return EventDto.builder()
        .id(event.getId())
        .name(event.getName())
        .description(event.getDescription())
        .date(event.getDate())
        .address(EventAddressMapper.toDto(event.getAddress()))
        .availableTickets(event.getAvailableTickets())
        .ticketPrice(event.getTicketPrice())
        .adult(event.getAdult())
        .build();
  }

}
