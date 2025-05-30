package com.event_app.service;

import com.event_app.dto.TicketDto;
import com.event_app.entity.Ticket;
import com.event_app.mapper.TicketMapper;
import com.event_app.repository.TicketRepository;
import com.event_app.utils.SecurityUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketService {

  private final TicketRepository repository;
  private final SecurityUtils securityUtils;
  private final EventService eventService;

  public TicketDto buyTicketForEvent(Long eventId, TicketDto dto) {
    log.info("Buying ticket for event with ID: {}", eventId);
    validateTicketPurchase(eventId, dto);

    var event = eventService.findEventById(eventId);

    var ticket = Ticket.builder()
        .name(dto.name())
        .surname(dto.surname())
        .event(event)
        .buyer(securityUtils.getCurrentUser())
        .build();

    var savedTicket = repository.save(ticket);
    return TicketMapper.toDto(savedTicket);
  }

  public List<TicketDto> getMyTickets() {
    log.info("Fetching tickets for current user");
    var user = securityUtils.getCurrentUser();
    return user.getTickets().stream()
        .map(TicketMapper::toDto)
        .toList();
  }

  public TicketDto getTicketById(Long ticketId) {
    log.info("Fetching ticket with ID: {}", ticketId);

    var ticket = findTicketById(ticketId);

    if (!securityUtils.getCurrentUser().getTickets().contains(ticket)) {
      throw new IllegalStateException("You can only view your own tickets");
    }

    return TicketMapper.toDto(ticket);
  }

  public TicketDto editTicketNameAndSurname(Long ticketId, TicketDto dto) {
    log.info("Editing ticket with ID: {}", ticketId);
    var ticket = findTicketById(ticketId);

    if (!securityUtils.getCurrentUser().getTickets().contains(ticket)) {
      throw new IllegalStateException("You can only edit your own tickets");
    }

    validateTicketUpdate(ticket.getEvent().getId(), dto, ticket);

    ticket.setName(dto.name()).setSurname(dto.surname());
    var updatedTicket = repository.save(ticket);
    return TicketMapper.toDto(updatedTicket);
  }

  public void deleteTicket(Long ticketId) {
    log.info("Deleting ticket with ID: {}", ticketId);
    var ticket = findTicketById(ticketId);

    if (!securityUtils.getCurrentUser().getTickets().contains(ticket)) {
      throw new IllegalStateException("You can only delete your own tickets");
    }

    repository.delete(ticket);
  }

  private Ticket findTicketById(Long ticketId) {
    return repository.findById(ticketId)
        .orElseThrow(
            () -> new EntityNotFoundException("Ticket with id " + ticketId + " not found"));
  }

  private void validateTicketPurchase(Long eventId, TicketDto dto) {
    var purchasedTicketsForEvent = securityUtils.getCurrentUser().getTickets()
        .stream()
        .filter(ticket -> ticket.getEvent().getId().equals(eventId))
        .count();

    if (purchasedTicketsForEvent >= 5) {
      throw new IllegalStateException("You cannot buy more than 5 tickets for the same event");
    }

    if (matchingTicketForEvent(eventId, dto) != null) {
      throw new EntityExistsException(
          "You already have a ticket with the same name and surname for this event");
    }
  }

  private void validateTicketUpdate(Long eventId, TicketDto dto, Ticket ticketToUpdate) {
    var matchingTicket = matchingTicketForEvent(eventId, dto);

    if (!isTicketOwner(ticketToUpdate)) {
      throw new IllegalStateException("You can only edit your own tickets");
    }

    if (matchingTicket != null && !matchingTicket.getId().equals(ticketToUpdate.getId())) {
      throw new EntityExistsException(
          "You already have a ticket with the same name and surname for this event");
    }
  }

  private Ticket matchingTicketForEvent(Long eventId, TicketDto dto) {
    return securityUtils.getCurrentUser().getTickets()
        .stream()
        .filter(ticket -> ticket.getEvent().getId().equals(eventId) &&
            Objects.equals(ticket.getName(), dto.name()) &&
            Objects.equals(ticket.getSurname(), dto.surname()))
        .findFirst()
        .orElse(null);
  }

  private boolean isTicketOwner(Ticket ticket) {
    return securityUtils.getCurrentUser().getTickets().contains(ticket);
  }
}
