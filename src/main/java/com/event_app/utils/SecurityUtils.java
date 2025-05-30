package com.event_app.utils;

import com.event_app.entity.User;
import com.event_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

  private final UserRepository userRepository;

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      String username = ((UserDetails) authentication.getPrincipal()).getUsername();
      return userRepository.findByEmail(username)
          .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
    return null;
  }
}

