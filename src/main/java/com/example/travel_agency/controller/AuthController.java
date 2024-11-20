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
        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь уже существует");
            return "redirect:/register";
        }

        userService.save(user);
        session.setAttribute("loggedInUser", user);
        redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно");
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
            redirectAttributes.addFlashAttribute("errorMessage", "Неправильный логин или пароль");
            model.addText("user");
            redirectAttributes.
                    addFlashAttribute("actionMessage", "Вход выполнен успешно");
            return "redirect:/";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
