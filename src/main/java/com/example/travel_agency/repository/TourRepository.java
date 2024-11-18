package com.example.travel_agency.repository;

import com.example.travel_agency.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    // Поиск тура по ключевым словам (название, описание, направление)
    @Query("SELECT t FROM Tour t WHERE CONCAT(t.name, ' ', t.description, ' ', t.destination) LIKE %?1%")
    List<Tour> search(String keyword);

    // Найти туры в диапазоне дат
    @Query("SELECT t FROM Tour t WHERE t.startDate BETWEEN :startDate AND :endDate")
    List<Tour> findByStartDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Получить данные для гистограммы количества туров по начальной дате
    @Query("SELECT t.startDate, COUNT(t) FROM Tour t GROUP BY t.startDate ORDER BY t.startDate")
    List<Object[]> getStartDateHistogramData();

    // Получить общее количество туров
    @Query("SELECT COUNT(t) FROM Tour t")
    int getTotalTourCount();

    // Подсчет количества туров за каждый день (по дате начала)
    @Query("SELECT t.startDate, COUNT(t) FROM Tour t GROUP BY t.startDate")
    List<Object[]> getToursPerDay();
}
