package com.example.travel_agency.controller;

import com.example.travel_agency.model.Tour;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.travel_agency.model.User;
import com.example.travel_agency.service.UserService;
import com.example.travel_agency.service.TourService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            addHomePageAttributes(model, session);
            model.addAttribute("registrationError", "Ошибка регистрации");
            return "index";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("registrationError", "Пользователь с таким именем уже существует");
            model.addAttribute("user", user);
            addHomePageAttributes(model, session);
            return "index";
        }

        // Сохраняем пользователя с выбранной ролью
        userService.save(user);

        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("actionMessage", "Регистрация прошла успешно");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && userService.checkPassword(existingUser, user.getPassword())) {
            session.setAttribute("loggedInUser", existingUser);
            redirectAttributes.addFlashAttribute("actionMessage", "Вход выполнен успешно");
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "Неверное имя пользователя или пароль");
            model.addAttribute("user", new User());
            addHomePageAttributes(model, session);
            return "index";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("actionMessage", "Вы успешно вышли из системы");
        return "redirect:/";
    }

    private void addHomePageAttributes(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Tour> listTours = tourService.listAll(null);
        model.addAttribute("listTours", listTours);
        model.addAttribute("loggedInUser", loggedInUser);
        int totalTourCount = tourService.getTotalTourCount();
        model.addAttribute("totalTourCount", totalTourCount);
    }
}

