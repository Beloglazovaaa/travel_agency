package com.example.travel_agency.controller;

import ch.qos.logback.core.model.Model;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes redirectAttributes) {
        // Проверка на пустые поля
        if (user.getUsername() == null || user.getUsername().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank() ||
                user.getRole() == null || user.getRole().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Все поля обязательны для заполнения");
            return "redirect:/";
        }

        // Проверка минимальной длины имени пользователя и пароля
        if (user.getUsername().length() < 4 || user.getPassword().length() < 4) {
            redirectAttributes.addFlashAttribute("errorMessage", "Имя пользователя и пароль должны быть не менее 4 символов");
            return "redirect:/";
        }

        // Проверка на уникальность имени пользователя
        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь уже существует");
            return "redirect:/";
        }

        // Сохранение пользователя с хешированием пароля
        userService.save(user);
        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно");
        return "redirect:/";
    }



    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && userService.checkPassword(existingUser, user.getPassword())) {
            session.setAttribute("loggedInUser", existingUser);
            // Перенаправление в зависимости от роли
            if ("ADMIN".equalsIgnoreCase(existingUser.getRole())) {
                return "redirect:/admin";
            } else if ("AGENT".equalsIgnoreCase(existingUser.getRole())) {
                return "redirect:/agent";
            } else if ("USER".equalsIgnoreCase(existingUser.getRole())) {
                return "redirect:/user";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Неправильный логин или пароль");
        return "redirect:/";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
