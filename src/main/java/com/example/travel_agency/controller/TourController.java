package com.example.travel_agency.controller;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/toursByRole")
    public String getToursByRole(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            model.addAttribute("errorMessage", "Вы не авторизованы");
            return "redirect:/login";
        }

        List<Tour> tours = tourService.getToursByUserRole(currentUser);
        model.addAttribute("listTours", tours);

        return "tours";
    }


    // Вспомогательный метод для получения пользователя
    private User getCurrentUser(Principal principal) {
        // Реализуйте получение пользователя из Principal
        return new User(); // Пример
    }
}
