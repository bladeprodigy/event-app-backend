package com.event_app.auth;

import lombok.Builder;

@Builder
public record LoginRequest(
    String email,
    String password
) {

}
