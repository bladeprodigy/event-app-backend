package com.event_app.auth;

public record LoginRequest(
    String email,
    String password
) {

}
