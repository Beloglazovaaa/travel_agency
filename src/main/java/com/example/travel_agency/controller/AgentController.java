package com.example.travel_agency.controller;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.TourService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                           @RequestParam("images") MultipartFile[] images,
                           HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.error("Попытка сохранения тура без авторизации.");
            throw new RuntimeException("Пользователь не авторизован.");
        }

        tour.setCreatedBy(loggedInUser);

        // Обработка загрузки изображений
        if (images != null && images.length > 0) {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        // Генерируем уникальное имя файла
                        String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                        // Указываем путь для сохранения
                        Path uploadPath = Paths.get("uploads", filename);
                        // Создаем директорию, если её нет
                        Files.createDirectories(uploadPath.getParent());
                        // Сохраняем файл
                        Files.copy(image.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
                        // Добавляем путь к файлу в список
                        imagePaths.add(filename);
                    } catch (IOException e) {
                        logger.error("Ошибка при сохранении изображения: {}", e.getMessage());
                    }
                }
            }
            tour.setImages(imagePaths);
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
}
