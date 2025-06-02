package com.event_app.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.event_app.dto.EventAddressDto;
import com.event_app.dto.EventDto;
import com.event_app.dto.TicketDto;
import com.event_app.entity.Event;
import com.event_app.entity.EventAddress;
import com.event_app.entity.Ticket;
import com.event_app.entity.User;
import com.event_app.repository.TicketRepository;
import com.event_app.service.EventService;
import com.event_app.service.TicketService;
import com.event_app.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private SecurityUtils securityUtils;

  @Mock
  private EventService eventService;

  @InjectMocks
  private TicketService ticketService;

  private User testUser;
  private Event testEvent;
  private Ticket testTicket;
  private TicketDto testTicketDto;
  private EventDto testEventDto;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setTickets(new ArrayList<>());

    EventAddressDto addressDto = EventAddressDto.builder()
        .id(1L)
        .name("Test Arena")
        .city("Test City")
        .street("Test Street")
        .postCode("12345")
        .build();

    EventAddress eventAddress = EventAddress.builder()
        .id(1L)
        .name("Test Arena")
        .city("Test City")
        .street("Test Street")
        .postCode("12345")
        .build();

    testEventDto = EventDto.builder()
        .id(1L)
        .name("Test Event")
        .description("Test Description")
        .date(LocalDateTime.now().plusDays(1))
        .availableTickets(100)
        .ticketPrice(50.0)
        .adult(true)
        .address(addressDto)
        .build();

    testEvent = Event.builder()
        .id(1L)
        .name("Test Event")
        .description("Test Description")
        .date(LocalDateTime.now().plusDays(1))
        .availableTickets(100)
        .ticketPrice(50.0)
        .adult(true)
        .address(eventAddress)
        .build();

    testTicket = Ticket.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .event(testEvent)
        .buyer(testUser)
        .build();

    testTicketDto = TicketDto.builder()
        .id(1L)
        .name("John")
        .surname("Doe")
        .event(testEventDto)
        .build();

    when(securityUtils.getCurrentUser()).thenReturn(testUser);
  }

  @Test
  void buyTicketForEvent_WhenValid_ShouldCreateTicket() {
    when(eventService.findEventById(1L)).thenReturn(testEvent);
    when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

    TicketDto result = ticketService.buyTicketForEvent(1L, testTicketDto);

    assertNotNull(result);
    assertEquals(testTicketDto.name(), result.name());
    assertEquals(testTicketDto.surname(), result.surname());
    assertEquals(testTicketDto.event().id(), result.event().id());
    verify(ticketRepository).save(any(Ticket.class));
  }

  @Test
  void getMyTickets_ShouldReturnUserTickets() {
    testUser.getTickets().add(testTicket);

    List<TicketDto> result = ticketService.getMyTickets();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(testTicketDto.name(), result.getFirst().name());
    assertEquals(testTicketDto.event().id(), result.getFirst().event().id());
  }

  @Test
  void getTicketById_WhenTicketExistsAndOwned_ShouldReturnTicket() {
    testUser.getTickets().add(testTicket);
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(testTicket));

    TicketDto result = ticketService.getTicketById(1L);

    assertNotNull(result);
    assertEquals(testTicketDto.name(), result.name());
    assertEquals(testTicketDto.event().id(), result.event().id());
    verify(ticketRepository).findById(1L);
  }

  @Test
  void getTicketById_WhenTicketNotOwned_ShouldThrowException() {
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(testTicket));

    assertThrows(IllegalStateException.class, () -> ticketService.getTicketById(1L));
    verify(ticketRepository).findById(1L);
  }

  @Test
  void editTicketNameAndSurname_WhenValid_ShouldUpdateTicket() {
    testUser.getTickets().add(testTicket);
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(testTicket));
    when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

    TicketDto updatedDto = TicketDto.builder()
        .id(1L)
        .name("Jane")
        .surname("Smith")
        .event(testEventDto)
        .build();

    TicketDto result = ticketService.editTicketNameAndSurname(1L, updatedDto);

    assertNotNull(result);
    assertEquals(updatedDto.name(), result.name());
    assertEquals(updatedDto.surname(), result.surname());
    assertEquals(updatedDto.event().id(), result.event().id());
    verify(ticketRepository).save(any(Ticket.class));
  }

  @Test
  void deleteTicket_WhenTicketExistsAndOwned_ShouldDeleteTicket() {
    testUser.getTickets().add(testTicket);
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(testTicket));
    doNothing().when(ticketRepository).delete(any(Ticket.class));

    assertDoesNotThrow(() -> ticketService.deleteTicket(1L));
    verify(ticketRepository).findById(1L);
    verify(ticketRepository).delete(testTicket);
  }

  @Test
  void deleteTicket_WhenTicketNotOwned_ShouldThrowException() {
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(testTicket));

    assertThrows(IllegalStateException.class, () -> ticketService.deleteTicket(1L));
    verify(ticketRepository).findById(1L);
    verify(ticketRepository, never()).delete(any(Ticket.class));
  }
}
