package com.example.travel_agency.controller;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Controller
@RequestMapping("/agent")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);
    private final TourService tourService;

    public AgentController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public String viewAgentTours(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.error("Попытка доступа к /tours без авторизации.");
            return "redirect:/login";
        }

        List<Tour> tours = tourService.findToursByAgent(loggedInUser);
        model.addAttribute("listTours", tours);
        model.addAttribute("page", "agentTours");
        return "agentTours";
    }

    @PostMapping("/saveTour")
    public String saveTour(@ModelAttribute("tour") Tour tour,
                           @RequestParam("imageFiles") MultipartFile[] imageFiles,
                           HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.error("Попытка сохранения тура без авторизации.");
            throw new RuntimeException("Пользователь не авторизован.");
        }

        tour.setCreatedBy(loggedInUser);

        if (imageFiles != null && imageFiles.length > 0) {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile image : imageFiles) {
                if (!image.isEmpty()) {
                    try {
                        // Генерируем уникальное имя файла
                        String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                        // Путь для сохранения
                        Path uploadPath = Paths.get("uploads");
                        Files.createDirectories(uploadPath);
                        // Сохраняем файл
                        Path filePath = uploadPath.resolve(filename);
                        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        // Добавляем имя файла в список
                        imagePaths.add(filename);
                    } catch (IOException e) {
                        logger.error("Ошибка при сохранении изображения: {}", e.getMessage());
                    }
                }
            }
            // Инициализируем список изображений, если он null
            if (tour.getImages() == null) {
                tour.setImages(new ArrayList<>());
            }
            tour.getImages().addAll(imagePaths);
        }

        tourService.save(tour);

        logger.info("Тур успешно сохранен: {}", tour);
        return "redirect:/agent/tours";
    }

    @PostMapping("/deleteTour")
    public String deleteTour(@RequestParam("id") Long id) {
        logger.info("Удаление тура с ID: {}", id);
        tourService.delete(id);
        return "redirect:/agent/tours";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("images");
    }
}

