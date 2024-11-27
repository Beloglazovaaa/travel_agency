package com.example.travel_agency.controller;

import com.example.travel_agency.model.*;
import com.example.travel_agency.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    private final TourService tourService;
    private final TourRequestService tourRequestService;
    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public AgentController(TourService tourService, TourRequestService tourRequestService,
                           UserService userService, BookingService bookingService) {
        this.tourService = tourService;
        this.tourRequestService = tourRequestService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    // Показать туры агента
    @GetMapping("/tours")
    public String viewAgentTours(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"AGENT".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "У вас нет доступа к этой странице.");
            return "redirect:/login";
        }

        List<Tour> tours = tourService.findToursByAgent(loggedInUser);
        model.addAttribute("listTours", tours);
        model.addAttribute("page", "agentTours");
        return "agentTours";
    }

    // Сохранить тур
    @PostMapping("/saveTour")
    public String saveTour(@ModelAttribute("tour") Tour tour,
                           @RequestParam("imageFiles") MultipartFile[] imageFiles,
                           HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"AGENT".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "У вас нет прав для выполнения этого действия.");
            return "redirect:/login";
        }

        tour.setCreatedBy(loggedInUser);

        if (imageFiles != null && imageFiles.length > 0) {
            List<String> imagePaths = new ArrayList<>();
            try {
                Path uploadPath = Paths.get("uploads");
                Files.createDirectories(uploadPath);

                for (MultipartFile image : imageFiles) {
                    if (!image.isEmpty()) {
                        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                        Path filePath = uploadPath.resolve(filename);
                        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        imagePaths.add(filename);
                    }
                }
            } catch (IOException e) {
                logger.error("Ошибка сохранения изображения: {}", e.getMessage());
            }

            if (tour.getImages() == null) {
                tour.setImages(new ArrayList<>());
            }
            tour.getImages().addAll(imagePaths);
        }

        tourService.save(tour);
        logger.info("Тур успешно сохранен: {}", tour);
        redirectAttributes.addFlashAttribute("successMessage", "Тур успешно сохранен!");
        return "redirect:/agent/tours";
    }

    // Удалить тур
    @PostMapping("/deleteTour")
    public String deleteTour(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            tourService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Тур успешно удален!");
        } catch (Exception e) {
            logger.error("Ошибка удаления тура: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка удаления тура.");
        }
        return "redirect:/agent/tours";
    }

    // Просмотр заявок
    @GetMapping("/requests")
    public String viewRequests(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"AGENT".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "У вас нет доступа к заявкам.");
            return "redirect:/login";
        }

        List<TourRequest> requests = tourRequestService.findAllNew();
        model.addAttribute("requests", requests);
        return "agentRequests";
    }

    // Создание клиента на основе заявки
    @PostMapping("/createClient")
    public String createClient(@RequestParam Long requestId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"AGENT".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "У вас нет прав для выполнения этого действия.");
            return "redirect:/login";
        }

        TourRequest request = tourRequestService.findById(requestId);
        if (request == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Заявка не найдена.");
            return "redirect:/agent/requests";
        }

        // Создание нового пользователя
        User newUser = new User();
        newUser.setUsername(request.getUserEmail());
        newUser.setPassword(generateRandomPassword());
        newUser.setRole("USER");
        userService.save(newUser);

        // Создание бронирования
        Tour tour = tourService.findById(request.getTourId());
        bookingService.bookTour(newUser, tour);

        // Обновление статуса заявки
        request.setStatus("Обработана");
        tourRequestService.save(request);

        redirectAttributes.addFlashAttribute("successMessage", "Клиент создан и тур забронирован!");
        return "redirect:/agent/requests";
    }

    // Генерация случайного пароля
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // Инициализация биндеров
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("images");
    }
}
