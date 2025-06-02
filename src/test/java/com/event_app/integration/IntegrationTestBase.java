package com.event_app.integration;

import com.event_app.TestDataFactory;
import com.event_app.repository.EventAddressRepository;
import com.event_app.repository.EventRepository;
import com.event_app.repository.TicketRepository;
import com.event_app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTestBase {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EventRepository eventRepository;

  @Autowired
  TicketRepository ticketRepository;

  @Autowired
  EventAddressRepository eventAddressRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    eventRepository.deleteAll();
    ticketRepository.deleteAll();

    userRepository.save(TestDataFactory.createUser());
    userRepository.save(TestDataFactory.createOrganizer());
    userRepository.save(TestDataFactory.createAdmin());
  }
}
