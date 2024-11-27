package com.example.travel_agency.controller;

import com.example.travel_agency.model.Booking;
import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.BookingService;
import com.example.travel_agency.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TourService tourService;

    @Autowired
    private BookingService bookingService;

    // Просмотр доступных туров
    @GetMapping("/tours")
    public String viewAvailableTours(Model model) {
        List<Tour> tours = tourService.listAvailableTours();
        model.addAttribute("listTours", tours);
        model.addAttribute("page", "userTours");
        return "userTours"; // шаблон userTours.html
    }

    // Просмотр своих бронирований
    @GetMapping("/bookings")
    public String viewMyBookings(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Booking> bookings = bookingService.getBookingsByUser(loggedInUser);
        model.addAttribute("bookings", bookings);
        model.addAttribute("noBookingsMessage", bookings.isEmpty() ? "У вас пока нет бронирований" : null);
        return "myBookings"; // шаблон myBookings.html
    }

    // Бронирование тура
    @PostMapping("/bookTour")
    public String bookTour(@RequestParam("tourId") Long tourId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Tour tour = tourService.findById(tourId);
        bookingService.bookTour(loggedInUser, tour);
        redirectAttributes.addFlashAttribute("successMessage", "Тур успешно забронирован");
        return "redirect:/user/bookings";
    }
}

