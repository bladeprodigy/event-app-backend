package com.event_app.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record EventAddressDto(
    Long id,
    @Size(min = 2, max = 50, message = "Event arena name must be between 2 and 25 characters")
    String name,
    @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
    String city,
    @Size(min = 2, max = 50, message = "Street name must be between 2 and 50 characters")
    String street,
    @Size(min = 2, max = 10, message = "Post code must be between 2 and 10 characters")
    String postCode
) {

}
