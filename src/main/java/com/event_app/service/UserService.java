package com.event_app.service;

import com.event_app.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

  private final SecurityUtils securityUtils;

  public String me() {
    return securityUtils.getCurrentUser().getRole().toString();
  }
}
