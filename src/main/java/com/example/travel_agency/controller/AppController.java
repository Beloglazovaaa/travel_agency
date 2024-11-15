package com.example.travel_agency.controller;

import com.example.travel_agency.model.Agency;
import com.example.travel_agency.model.User;
import com.example.travel_agency.service.AgencyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private AgencyService agencyService;

    @RequestMapping("/")
    public String viewHomePage(Model model, @Param("keyword") String keyword, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        List<Agency> listTours = AgencyService.listAll(keyword);
        model.addAttribute("listTours", listTours);
        model.addAttribute("keyword", keyword);
        model.addAttribute("loggedInUser", loggedInUser);

        int totalTourCount = AgencyService.getTotalTourCount();
        model.addAttribute("totalTourCount", totalTourCount);

        // Добавляем пустой объект User для модальных окон
        model.addAttribute("user", new User());

        return "index";
    }

    @PostMapping("/saveTour")
    public String saveTour(@ModelAttribute("tour") Agency agency, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || (!"ADMIN".equals(loggedInUser.getRole()) && !"EMPLOYEE".equals(loggedInUser.getRole()))) {
            return "redirect:/";
        }
        AgencyService.save(tour);
        redirectAttributes.addFlashAttribute("actionMessage", "Тур успешно сохранен");
        return "redirect:/";
    }

    @PostMapping("/deleteTour")
    public String deleteTour(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/";
        }
        AgencyService.delete(id);
        redirectAttributes.addFlashAttribute("actionMessage", "Тур успешно удален");
        return "redirect:/";
    }

    @GetMapping("/filterByStartDate")
    public String filterByStartDate(@RequestParam("startDate") String startDate,
                                    @RequestParam("endDate") String endDate,
                                    Model model,
                                    HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Agency> listTours = AgencyService.findByStartDateRange(start, end);
        model.addAttribute("listTours", listTours);

        return "index";
    }

    @GetMapping("/api/startDateHistogramData")
    @ResponseBody
    public List<Map<String, Object>> getStartDateHistogramData(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole())) {
            return new ArrayList<>(); // Возвращаем пустой список, если пользователь не администратор
        }

        List<Object[]> data = AgencyService.getStartDateHistogramData();
        List<Map<String, Object>> histogramData = new ArrayList<>();
        for (Object[] obj : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", obj[0].toString());
            map.put("count", ((Number) obj[1]).intValue());
            histogramData.add(map);
        }
        return histogramData;
    }

    @GetMapping("/api/totalTourCount")
    @ResponseBody
    public int getTotalTourCount() {
        return AgencyService.getTotalTourCount();
    }

    @GetMapping("/api/toursPerDay")
    @ResponseBody
    public List<Map<String, Object>> getToursPerDay() {
        List<Object[]> data = AgencyService.getToursPerDay();
        List<Map<String, Object>> toursData = new ArrayList<>();
        for (Object[] obj : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", obj[0].toString());
            map.put("count", ((Number) obj[1]).intValue());
            toursData.add(map);
        }
        return toursData;
    }
}
