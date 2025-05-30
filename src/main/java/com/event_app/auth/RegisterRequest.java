package com.event_app.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Email(
        message = "Email should be valid and not blank."
    )
    String email,
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message =
            "Password must be at least 8 characters long, contain at least one uppercase letter,"
                + " one lowercase letter, one digit, and one special character."
    )
    String password,
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long.")
    String name,
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long.")
    String surname,
    @Pattern(
        regexp = "^\\+?[0-9]{10,15}$",
        message = "Phone number must be 10 and 15 digits long and can start with a +."
    )
    String phoneNumber
) {

}
