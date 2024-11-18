package com.example.travel_agency.controller;

import com.example.travel_agency.model.*;
import com.example.travel_agency.service.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
public class AppController {

    @Autowired
    private TourService tourService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewService reviewService;

    // Главная страница
    @GetMapping("/")
    public String viewHomePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("page", "home");

        // Получение популярных туров
        List<Tour> popularTours = tourService.getPopularTours();
        model.addAttribute("popularTours", popularTours);

        return "base";
    }

    @GetMapping("/tours")
    public String viewToursPage(Model model,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "sortBy", required = false) String sortBy,
                                HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("page", "tours");

        List<Tour> listTours;

        if (sortBy != null) {
            if ("price".equals(sortBy)) {
                listTours = tourService.findAllSortedByPrice();
            } else if ("startDate".equals(sortBy)) {
                listTours = tourService.findAllSortedByStartDate();
            } else if ("rating".equals(sortBy)) {
                listTours = tourService.findAllSortedByRating();
            } else {
                listTours = tourService.listAll(keyword);
            }
        } else {
            listTours = tourService.listAll(keyword);
        }

        model.addAttribute("listTours", listTours);
        model.addAttribute("keyword", keyword);

        return "tours";
    }

    // Сохранение тура (добавление/редактирование)
    @PostMapping("/saveTour")
    public String saveTour(@ModelAttribute("tour") Tour tour, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || (!"ADMIN".equalsIgnoreCase(loggedInUser.getRole()) && !"AGENT".equalsIgnoreCase(loggedInUser.getRole()))) {
            return "redirect:/";
        }
        tourService.save(tour);
        redirectAttributes.addFlashAttribute("actionMessage", "Тур успешно сохранён");
        return "redirect:/tours";
    }

    // Удаление тура
    @PostMapping("/deleteTour")
    public String deleteTour(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/";
        }
        tourService.delete(id);
        redirectAttributes.addFlashAttribute("actionMessage", "Тур успешно удалён");
        return "redirect:/tours";
    }

    // Фильтрация туров по дате начала
    @GetMapping("/filterByStartDate")
    public String filterByStartDate(@RequestParam("startDate") String startDate,
                                    @RequestParam("endDate") String endDate,
                                    Model model,
                                    HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("page", "tours");

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Tour> listTours = tourService.findByStartDateRange(start, end);
        model.addAttribute("listTours", listTours);

        return "tours";
    }

    // Страница клиентов
    @GetMapping("/clients")
    public String viewClientsPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || (!"ADMIN".equalsIgnoreCase(loggedInUser.getRole()) && !"AGENT".equalsIgnoreCase(loggedInUser.getRole()))) {
            return "redirect:/";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("page", "clients");

        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);

        return "clients";
    }

    // Сохранение клиента
    @PostMapping("/saveClient")
    public String saveClient(@ModelAttribute("client") Client client, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || (!"ADMIN".equalsIgnoreCase(loggedInUser.getRole()) && !"AGENT".equalsIgnoreCase(loggedInUser.getRole()))) {
            return "redirect:/";
        }
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("actionMessage", "Клиент успешно сохранён");
        return "redirect:/clients";
    }

    // Удаление клиента
    @PostMapping("/deleteClient")
    public String deleteClient(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/";
        }
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("actionMessage", "Клиент успешно удалён");
        return "redirect:/clients";
    }

    // Добавить методы
    @PostMapping("/bookTour")
    public String bookTour(@RequestParam("tourId") Long tourId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "Необходимо войти в систему для бронирования тура");
            return "redirect:/";
        }
        Tour tour = tourService.findById(tourId);
        bookingService.bookTour(loggedInUser, tour);
        redirectAttributes.addFlashAttribute("actionMessage", "Тур успешно забронирован");
        return "redirect:/myBookings";
    }

    @GetMapping("/myBookings")
    public String viewMyBookings(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/";
        }
        List<Booking> bookings = bookingService.getBookingsByUser(loggedInUser);
        model.addAttribute("bookings", bookings);
        model.addAttribute("loggedInUser", loggedInUser);
        return "myBookings";
    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam("tourId") Long tourId,
                            @RequestParam("rating") int rating,
                            @RequestParam("comment") String comment,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("loginError", "Необходимо войти в систему для добавления отзыва");
            return "redirect:/";
        }
        Tour tour = tourService.findById(tourId);
        Review review = new Review(loggedInUser, tour, rating, comment);
        reviewService.saveReview(review);
        redirectAttributes.addFlashAttribute("actionMessage", "Отзыв успешно добавлен");
        return "redirect:/tours/" + tourId;
    }

    @GetMapping("/tours/{id}")
    public String viewTourDetails(@PathVariable("id") Long id, Model model, HttpSession session) {
        Tour tour = tourService.findById(id);
        List<Review> reviews = reviewService.getReviewsByTour(tour);
        model.addAttribute("tour", tour);
        model.addAttribute("reviews", reviews);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "tourDetails";
    }

}
