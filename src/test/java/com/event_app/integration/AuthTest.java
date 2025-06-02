package com.event_app.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.event_app.TestDataFactory;
import com.event_app.auth.LoginRequest;
import com.event_app.auth.RegisterRequest;
import com.event_app.entity.User;
import com.event_app.shared.Role;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;

public class AuthTest extends IntegrationTestBase {

  @Test
  void testRegisterUser_success() throws Exception {
    RegisterRequest request = TestDataFactory.createValidRegisterRequest();

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    List<User> users = userRepository.findAll();
    assertEquals(4, users.size());

    User newUser = users.getLast();
    assertEquals(request.email(), newUser.getEmail());
    assertEquals(Role.USER, newUser.getRole());
    assertNotNull(newUser.getPassword());
  }

  @Test
  void testRegisterUser_userAlreadyExists_throwsEntityAlreadyExistsException() throws Exception {
    RegisterRequest request = TestDataFactory
        .createRegisterRequest("user@mail.com", "ValidPassword123!");

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("User with this email already exists"));

    List<User> users = userRepository.findAll();
    assertEquals(3, users.size());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"     ", "invalidEmail", "invalidEmail.com", "invalidEmail@.com",
      "definitlyTooLongEmailToPassThisTest@muchTooLongEmail.com", "x@x.x"})
  void testRegisterUser_invalidEmail_throwsMethodArgumentNotValidException(String email)
      throws Exception {
    RegisterRequest request = TestDataFactory.createRegisterRequest(email, "ValidPassword123!");

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].message")
            .value("Email should be valid and not blank."));

    List<User> users = userRepository.findAll();
    assertEquals(3, users.size());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"iP123!", "invalidPassword", "invalidPassword123", "invalidPassword!@#",
      "invalidpassword123!", "INVALIDPASSWORD123!", "tooLongPasswordToPassThisTest123!@#"})
  void testRegisterUser_invalidPassword_throwsMethodArgumentNotValidException(String password)
      throws Exception {
    RegisterRequest request = TestDataFactory.createRegisterRequest("valid@email.com", password);

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].message")
            .value(
                "Password must be at least 8 characters long, contain at least one uppercase letter,"
                    + " one lowercase letter, one digit, and one special character."));

    List<User> users = userRepository.findAll();
    assertEquals(3, users.size());
  }

  @Test
  void testLogin_success() throws Exception {
    RegisterRequest registerRequest = TestDataFactory.createValidRegisterRequest();

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated());

    LoginRequest loginRequest = TestDataFactory
        .createLoginRequest(registerRequest.email(), registerRequest.password());

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token").isString());
  }
} 