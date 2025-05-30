package com.event_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record EventDto(
    Long id,
    @Size(min = 2, max = 50, message = "Event name must be between 2 and 50 characters")
    String name,
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,
    @NotNull(message = "Event date cannot be null")
    LocalDateTime date,
    @NotNull(message = "Event address cannot be null")
    EventAddressDto address,
    @NotNull(message = "Available tickets cannot be null")
    Integer availableTickets,
    @NotNull(message = "Ticket price cannot be null")
    Double ticketPrice,
    @NotNull(message = "Adult status cannot be null")
    Boolean adult
) {

}
