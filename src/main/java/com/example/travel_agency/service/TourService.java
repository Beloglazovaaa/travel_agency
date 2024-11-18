// TourService.java

package com.example.travel_agency.service;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

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
        if (keyword != null) {
            return tourRepository.findByNameContainingOrDescriptionContainingOrDestinationContaining(keyword, keyword, keyword);
        }
        return tourRepository.findAll();
    }

    // Поиск туров в диапазоне дат
    public List<Tour> findByStartDateRange(LocalDate start, LocalDate end) {
        return tourRepository.findByStartDateBetween(start, end);
    }

    // Получение общего количества туров
    public int getTotalTourCount() {
        return (int) tourRepository.count();
    }

    // Сортировка туров по цене
    public List<Tour> findAllSortedByPrice() {
        return tourRepository.findAllByOrderByPricePerPersonAsc();
    }

    // Сортировка туров по дате начала
    public List<Tour> findAllSortedByStartDate() {
        return tourRepository.findAllByOrderByStartDateAsc();
    }

    // Сортировка туров по рейтингу
    public List<Tour> findAllSortedByRating() {
        return tourRepository.findAllByOrderByRatingDesc();
    }

    // Поиск тура по ID
    public Tour findById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }
}
