package com.example.travel_agency.service;

import com.example.travel_agency.model.Tour;
import com.example.travel_agency.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервисный класс для управления турами агентства.
 */
@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    /**
     * Возвращает список всех туров, опционально фильтруя по ключевому слову.
     *
     * @param keyword ключевое слово для поиска
     * @return список туров
     */
    public List<Tour> listAll(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return tourRepository.search(keyword);
        }
        return tourRepository.findAll();
    }

    /**
     * Сохраняет или обновляет информацию о туре.
     *
     * @param tour объект тура для сохранения
     */
    public void save(Tour tour) {
        tourRepository.save(tour);
    }

    /**
     * Удаляет тур по его ID.
     *
     * @param id идентификатор тура
     */
    public void delete(Long id) {
        tourRepository.deleteById(id);
    }

    /**
     * Находит туры в заданном диапазоне дат.
     *
     * @param startDate начальная дата
     * @param endDate   конечная дата
     * @return список туров в диапазоне дат
     */
    public List<Tour> findByStartDateRange(LocalDate startDate, LocalDate endDate) {
        return tourRepository.findByStartDateRange(startDate, endDate);
    }

    /**
     * Получает данные для гистограммы количества туров по датам начала.
     *
     * @return список объектов с датами и количеством туров
     */
    public List<Object[]> getStartDateHistogramData() {
        return tourRepository.getStartDateHistogramData();
    }

    /**
     * Возвращает общее количество туров.
     *
     * @return общее количество туров
     */
    public int getTotalTourCount() {
        return tourRepository.getTotalTourCount();
    }

    /**
     * Получает данные о количестве туров на каждый день.
     *
     * @return список объектов с датами и количеством туров
     */
    public List<Object[]> getToursPerDay() {
        return tourRepository.getToursPerDay();
    }
}
