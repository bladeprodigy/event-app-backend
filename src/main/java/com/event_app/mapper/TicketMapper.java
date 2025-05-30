package com.event_app.mapper;

import com.event_app.dto.TicketDto;
import com.event_app.entity.Ticket;

public class TicketMapper {

  public static TicketDto toDto(Ticket ticket) {
    return TicketDto.builder()
        .id(ticket.getId())
        .name(ticket.getName())
        .surname(ticket.getSurname())
        .event(EventMapper.toDto(ticket.getEvent()))
        .build();
  }
}
