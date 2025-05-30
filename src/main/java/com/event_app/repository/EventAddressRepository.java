package com.event_app.repository;

import com.event_app.entity.EventAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventAddressRepository extends JpaRepository<EventAddress, Long> {

}
