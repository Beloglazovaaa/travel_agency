package com.example.travel_agency.repository;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // Поиск туров по ключевым словам
    List<Tour> findByNameContainingOrDescriptionContainingOrDestinationContaining(String name, String description, String destination);

    // Поиск туров в диапазоне дат
    List<Tour> findByStartDateBetween(LocalDate start, LocalDate end);

    // Топ-5 популярных туров
    List<Tour> findTop5ByOrderByRatingDesc();

    // Сортировка туров по цене
    List<Tour> findAllByOrderByPricePerPersonAsc();

    // Сортировка туров по дате начала
    List<Tour> findAllByOrderByStartDateAsc();

    // Сортировка туров по рейтингу
    List<Tour> findAllByOrderByRatingDesc();

    // Поиск туров, созданных определенным агентом
    List<Tour> findByCreatedBy(User createdBy);

    // Поиск доступных туров для пользователей
    List<Tour> findByAvailableSeatsGreaterThan(int availableSeats);

    
}

