package com.event_app.error;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record CustomError(
    HttpStatus status,
    String message,
    LocalDateTime timestamp
) {

}
