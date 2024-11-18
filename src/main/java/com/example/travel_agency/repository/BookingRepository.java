// BookingRepository.java

package com.example.travel_agency.repository;

import com.example.travel_agency.model.Booking;
import com.example.travel_agency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
