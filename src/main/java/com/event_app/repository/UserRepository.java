package com.event_app.repository;

import com.event_app.entity.User;
import com.event_app.shared.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByRole(Role role);
}
