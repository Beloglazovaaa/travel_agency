// TourRepository.java

package com.example.travel_agency.repository;

import com.example.travel_agency.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    // Поиск тура по ключевым словам
    List<Tour> findByNameContainingOrDescriptionContainingOrDestinationContaining(String name, String description, String destination);

    // Найти туры с начальной датой в заданном диапазоне
    List<Tour> findByStartDateBetween(LocalDate start, LocalDate end);

    // Получить топ-5 популярных туров по рейтингу
    List<Tour> findTop5ByOrderByRatingDesc();

    // Сортировка по цене
    List<Tour> findAllByOrderByPricePerPersonAsc();

    // Сортировка по дате начала
    List<Tour> findAllByOrderByStartDateAsc();

    // Сортировка по рейтингу
    List<Tour> findAllByOrderByRatingDesc();
}
