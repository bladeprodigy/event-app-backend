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
import com.event_app.entity.Event;
import com.event_app.entity.EventAddress;
import com.event_app.repository.EventRepository;
import com.event_app.service.EventAddressService;
import com.event_app.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private EventAddressService eventAddressService;

  @InjectMocks
  private EventService eventService;

  private Event testEvent;
  private EventDto testEventDto;

  @BeforeEach
  void setUp() {
    EventAddressDto testAddressDto = EventAddressDto.builder()
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

    testEventDto = EventDto.builder()
        .id(1L)
        .name("Test Event")
        .description("Test Description")
        .date(LocalDateTime.now().plusDays(1))
        .availableTickets(100)
        .ticketPrice(50.0)
        .adult(true)
        .address(testAddressDto)
        .build();
  }

  @Test
  void getAllEvents_ShouldReturnListOfEvents() {
    when(eventRepository.findAll()).thenReturn(List.of(testEvent));

    List<EventDto> result = eventService.getAllEvents();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(testEventDto.name(), result.getFirst().name());
    verify(eventRepository).findAll();
  }

  @Test
  void getEventById_WhenEventExists_ShouldReturnEvent() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

    EventDto result = eventService.getEventById(1L);

    assertNotNull(result);
    assertEquals(testEventDto.name(), result.name());
    verify(eventRepository).findById(1L);
  }

  @Test
  void getEventById_WhenEventDoesNotExist_ShouldThrowException() {
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> eventService.getEventById(1L));
    verify(eventRepository).findById(1L);
  }

  @Test
  void createEvent_ShouldCreateAndReturnNewEvent() {
    EventAddress mockAddress = new EventAddress();
    when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
    when(eventAddressService.createEventAddress(any(), any())).thenReturn(mockAddress);

    EventDto result = eventService.createEvent(testEventDto);

    assertNotNull(result);
    assertEquals(testEventDto.name(), result.name());
    verify(eventRepository).save(any(Event.class));
    verify(eventAddressService).createEventAddress(any(), any());
  }

  @Test
  void deleteEvent_WhenEventExists_ShouldDeleteEvent() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
    doNothing().when(eventRepository).delete(any(Event.class));

    assertDoesNotThrow(() -> eventService.deleteEvent(1L));
    verify(eventRepository).findById(1L);
    verify(eventRepository).delete(testEvent);
  }

  @Test
  void deleteEvent_WhenEventDoesNotExist_ShouldThrowException() {
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> eventService.deleteEvent(1L));
    verify(eventRepository).findById(1L);
    verify(eventRepository, never()).delete(any(Event.class));
  }
}
