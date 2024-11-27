package com.example.travel_agency.service;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import com.example.travel_agency.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // Получение популярных туров
    public List<Tour> getPopularTours() {
        return tourRepository.findTop5ByOrderByRatingDesc();
    }

    // Сохранение тура
    public void save(Tour tour) {
        tourRepository.save(tour);
    }

    // Удаление тура
    public void delete(Long id) {
        tourRepository.deleteById(id);
    }

    // Поиск туров по ключевым словам
    public List<Tour> listAll(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return tourRepository.findByNameContainingOrDescriptionContainingOrDestinationContaining(keyword, keyword, keyword);
        }
        return tourRepository.findAll();
    }

    // Сортировка по цене
    public List<Tour> findAllSortedByPrice() {
        return tourRepository.findAllByOrderByPricePerPersonAsc();
    }

    // Сортировка по дате начала
    public List<Tour> findAllSortedByStartDate() {
        return tourRepository.findAllByOrderByStartDateAsc();
    }

    // Сортировка по рейтингу
    public List<Tour> findAllSortedByRating() {
        return tourRepository.findAllByOrderByRatingDesc();
    }

    // Поиск тура по ID
    public Tour findById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    // Поиск туров в диапазоне дат
    public List<Tour> findByStartDateRange(LocalDate start, LocalDate end) {
        return tourRepository.findByStartDateBetween(start, end);
    }

    // Получение туров, созданных агентом
    public List<Tour> findToursByAgent(User agent) {
        return tourRepository.findByCreatedBy(agent);
    }

    public List<Tour> getToursByUserRole(User user) {
        if (user == null) {
            return List.of(); // Пустой список для неавторизованных пользователей
        }

        String role = user.getRole();
        switch (role) {
            case "ADMIN":
                return tourRepository.findAll();
            case "AGENT":
                return findToursByAgent(user);
            case "USER":
                return tourRepository.findByAvailableSeatsGreaterThan(0);
            default:
                return List.of(); // Пустой список для неизвестных ролей
        }
    }

    public List<Tour> listAvailableTours() {
        return tourRepository.findByAvailableSeatsGreaterThan(0);
    }


    public List<Tour> getToursByUserRole(User user, String sortBy, String keyword) {
        if (user != null) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return getSortedTours(sortBy, keyword);
            } else if ("AGENT".equalsIgnoreCase(user.getRole())) {
                return findToursByAgent(user);
            }
        }
        return getSortedTours(sortBy, keyword);
    }

    private List<Tour> getSortedTours(String sortBy, String keyword) {
        if (sortBy != null) {
            switch (sortBy) {
                case "price":
                    return tourRepository.findAllByOrderByPricePerPersonAsc();
                case "startDate":
                    return tourRepository.findAllByOrderByStartDateAsc();
                case "rating":
                    return tourRepository.findAllByOrderByRatingDesc();
                default:
                    return tourRepository.findByNameContainingOrDescriptionContainingOrDestinationContaining(keyword, keyword, keyword);
            }
        }
        return tourRepository.findAll();
    }

}
