package com.event_app;

import com.event_app.auth.LoginRequest;
import com.event_app.auth.RegisterRequest;
import com.event_app.entity.Event;
import com.event_app.entity.EventAddress;
import com.event_app.entity.User;
import com.event_app.shared.Role;
import java.time.LocalDateTime;

public class TestDataFactory {

  public static User createUser() {
    return User.builder()
        .email("user@mail.com")
        .password("UserPassword123!")
        .role(Role.USER)
        .build();
  }

  public static User createOrganizer() {
    return User.builder()
        .email("organizer@mail.com")
        .password("OrganizerPassword123!")
        .role(Role.ORGANIZER)
        .build();
  }

  public static User createAdmin() {
    return User.builder()
        .email("admin@mail.com")
        .password("AdminPassword123!")
        .role(Role.ADMIN)
        .build();
  }

  public static Event createEvent() {
    return Event.builder()
        .name("Sample Event")
        .description("This is a sample event description.")
        .date(LocalDateTime.now())
        .ticketPrice(99.99)
        .availableTickets(100)
        .adult(false)
        .build();
  }

  public static EventAddress createEventAddress(Event event) {
    return EventAddress.builder()
        .name("Sample Venue")
        .street("123 Main St")
        .city("Sample City")
        .postCode("12345")
        .event(event)
        .build();
  }

  public static RegisterRequest createValidRegisterRequest() {
    return RegisterRequest.builder()
        .name("Test")
        .surname("User")
        .email("test.user@mail.com")
        .password("TestPassword123!")
        .phoneNumber("+48123456789")
        .build();
  }

  public static RegisterRequest createRegisterRequest(String email, String password) {
    return RegisterRequest.builder()
        .name("Test")
        .surname("User")
        .email(email)
        .password(password)
        .phoneNumber("+48123456789")
        .build();
  }

  public static LoginRequest createLoginRequest(String email, String password) {
    return LoginRequest.builder()
        .email(email)
        .password(password)
        .build();
  }
}
