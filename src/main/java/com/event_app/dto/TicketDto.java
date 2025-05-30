package com.event_app.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TicketDto(
    Long id,
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    String name,
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    String surname,
    EventDto event
) {

}
