package com.example.travel_agency.controller;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.TourService;
import com.example.travel_agency.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TourService tourService;

    // Просмотр всех туров
    @GetMapping("/tours")
    public String viewAllTours(Model model, HttpSession session) {
        List<Tour> tours = tourService.listAll(null);
        model.addAttribute("listTours", tours);
        model.addAttribute("page", "adminTours");
        return "adminTours"; // шаблон adminTours.html
    }

    // Удаление тура
    @PostMapping("/deleteTour")
    public String deleteTour(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        tourService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Тур успешно удалён");
        return "redirect:/admin/tours";
    }

    // Добавление или редактирование тура
    @PostMapping("/saveTour")
    public String saveTour(@ModelAttribute("tour") Tour tour, RedirectAttributes redirectAttributes) {
        tourService.save(tour);
        redirectAttributes.addFlashAttribute("successMessage", "Тур успешно сохранён");
        return "redirect:/admin/tours";
    }

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users"; // Страница с пользователями
    }

    @PostMapping("/update-role/{id}")
    public String updateRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/users"; // Перенаправление на страницу пользователей
    }

    @PostMapping("/toggle-ban/{id}")
    public String toggleBan(@PathVariable Long id) {
        userService.toggleBan(id);
        return "redirect:/admin/users"; // Перенаправление обратно на страницу пользователей
    }
}
