package com.event_app.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.event_app.TestDataFactory;
import com.event_app.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;

public class EventTest extends IntegrationTestBase {

  @Test
  @WithAnonymousUser
  void testGetAllEvents_success() throws Exception {
    Event event = eventRepository.save(TestDataFactory.createEvent());
    eventAddressRepository.save(TestDataFactory.createEventAddress(event));

    mockMvc.perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(event.getId()))
        .andExpect(jsonPath("$[0].name").value(event.getName()))
        .andExpect(jsonPath("$[0].description").value(event.getDescription()))
        .andExpect(jsonPath("$[0].date").isNotEmpty())
        .andExpect(jsonPath("$[0].address").isNotEmpty())
        .andExpect(jsonPath("$[0].availableTickets").value(event.getAvailableTickets()))
        .andExpect(jsonPath("$[0].ticketPrice").value(event.getTicketPrice()))
        .andExpect(jsonPath("$[0].adult").value(event.getAdult()));
  }

  @Test
  void testGetEventById_success() throws Exception {
    Event event = eventRepository.save(TestDataFactory.createEvent());
    eventAddressRepository.save(TestDataFactory.createEventAddress(event));

    mockMvc.perform(get("/events/" + event.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(event.getId()))
        .andExpect(jsonPath("$.name").value(event.getName()))
        .andExpect(jsonPath("$.description").value(event.getDescription()))
        .andExpect(jsonPath("$.date").isNotEmpty())
        .andExpect(jsonPath("$.address").isNotEmpty())
        .andExpect(jsonPath("$.availableTickets").value(event.getAvailableTickets()))
        .andExpect(jsonPath("$.ticketPrice").value(event.getTicketPrice()))
        .andExpect(jsonPath("$.adult").value(event.getAdult()));
  }

  @Test
  void testGetEventById_notFound() throws Exception {
    mockMvc.perform(get("/events/9999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Event with id 9999 not found"));
  }
}
