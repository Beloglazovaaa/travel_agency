// BookingService.java

package com.example.travel_agency.service;

import com.example.travel_agency.model.Booking;
import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public void bookTour(User user, Tour tour) {
        Booking booking = new Booking(user, tour);
        bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
}
