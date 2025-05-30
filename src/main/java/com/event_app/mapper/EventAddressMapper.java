package com.event_app.mapper;

import com.event_app.dto.EventAddressDto;
import com.event_app.entity.EventAddress;

public class EventAddressMapper {

  public static EventAddressDto toDto(EventAddress eventAddress) {
    return EventAddressDto.builder()
        .id(eventAddress.getId())
        .name(eventAddress.getName())
        .city(eventAddress.getCity())
        .street(eventAddress.getStreet())
        .postCode(eventAddress.getPostCode())
        .build();
  }
}
