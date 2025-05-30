package com.event_app.utils;

import com.event_app.entity.User;
import com.event_app.repository.UserRepository;
import com.event_app.shared.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

  private final UserRepository userRepository;

  @EventListener
  public void seedAdmin(ContextRefreshedEvent event) {
    if (!userRepository.existsByRole(Role.ADMIN)) {
      User admin = new User();
      admin.setEmail("admin@admin.admin");
      admin.setPassword(new BCryptPasswordEncoder().encode("Admin123!"));
      admin.setName("admin");
      admin.setSurname("admin");
      admin.setRole(Role.ADMIN);
      userRepository.save(admin);
    }
  }

  @EventListener
  public void seed(ContextRefreshedEvent event) {
    if (!userRepository.existsByRole(Role.ORGANIZER)) {
      User organizer = new User();
      organizer.setEmail("org@org.org");
      organizer.setPassword(new BCryptPasswordEncoder().encode("Organizer123!"));
      organizer.setName("org");
      organizer.setSurname("org");
      organizer.setRole(Role.ORGANIZER);
      userRepository.save(organizer);
    }
  }
}
