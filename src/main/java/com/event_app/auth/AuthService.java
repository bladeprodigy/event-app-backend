package com.event_app.auth;

import com.event_app.entity.User;
import com.event_app.repository.UserRepository;
import com.event_app.security.JwtService;
import com.event_app.shared.Role;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public void register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EntityExistsException("User with this email already exists");
    }

    User user = User.builder()
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .name(request.name())
        .surname(request.surname())
        .role(Role.USER)
        .build();

    userRepository.save(user);
    log.info("User registered: {}", user);
  }

  public TokenResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.email(),
            request.password()
        )
    );
    User authenticatedUser = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new EntityNotFoundException("Invalid email or password"));
    String jwtToken = jwtService.generateToken(authenticatedUser);
    log.info("User logged in: {}", authenticatedUser);
    return new TokenResponse(jwtToken);
  }
}
