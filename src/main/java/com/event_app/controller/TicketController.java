package com.event_app.controller;

import com.event_app.dto.TicketDto;
import com.event_app.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
class TicketController {

  private final TicketService ticketService;

  @GetMapping
  ResponseEntity<List<TicketDto>> getMyTickets() {
    var tickets = ticketService.getMyTickets();
    return ResponseEntity.ok(tickets);
  }

  @GetMapping("/{ticketId}")
  ResponseEntity<TicketDto> getTicketById(@PathVariable Long ticketId) {
    var ticket = ticketService.getTicketById(ticketId);
    return ResponseEntity.ok(ticket);
  }

  @PostMapping("/{eventId}")
  ResponseEntity<TicketDto> buyTicketForEvent(@PathVariable Long eventId, TicketDto ticketDto) {
    var ticket = ticketService.buyTicketForEvent(eventId, ticketDto);
    return ResponseEntity.status(201).body(ticket);
  }

  @PutMapping("/{ticketId}")
  ResponseEntity<TicketDto> updateTicket(@PathVariable Long ticketId, TicketDto ticketDto) {
    var updatedTicket = ticketService.editTicketNameAndSurname(ticketId, ticketDto);
    return ResponseEntity.ok(updatedTicket);
  }

  @DeleteMapping("/{ticketId}")
  ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
    ticketService.deleteTicket(ticketId);
    return ResponseEntity.noContent().build();
  }
}
