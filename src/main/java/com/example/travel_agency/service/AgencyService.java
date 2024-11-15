package com.example.travel_agency.service;

import com.example.travel_agency.model.Agency;
import com.example.travel_agency.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервисный класс для управления турами агентства.
 */
@Service
public class AgencyService {

    @Autowired
    private AgencyRepository agencyRepository;

    /**
     * Возвращает список всех туров, опционально фильтруя по ключевому слову.
     *
     * @param keyword ключевое слово для поиска
     * @return список туров
     */
    public static List<Agency> listAll(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return agencyRepository.search(keyword);
        }
        return agencyRepository.findAll();
    }

    /**
     * Сохраняет или обновляет информацию о туре.
     *
     * @param agency объект тура для сохранения
     */
    public void save(Agency agency) {
        agencyRepository.save(agency);
    }

    /**
     * Удаляет тур по его ID.
     *
     * @param id идентификатор тура
     */
    public void delete(Long id) {
        agencyRepository.deleteById(id);
    }

    /**
     * Находит туры в заданном диапазоне дат.
     *
     * @param startDate начальная дата
     * @param endDate   конечная дата
     * @return список туров в диапазоне дат
     */
    public List<Agency> findByStartDateRange(LocalDate startDate, LocalDate endDate) {
        return agencyRepository.findByStartDateRange(startDate, endDate);
    }

    /**
     * Получает данные для гистограммы количества туров по датам начала.
     *
     * @return список объектов с датами и количеством туров
     */
    public List<Object[]> getStartDateHistogramData() {
        return agencyRepository.getStartDateHistogramData();
    }

    /**
     * Возвращает общее количество туров.
     *
     * @return общее количество туров
     */
    public static int getTotalTourCount() {
        return agencyRepository.getTotalTourCount();
    }

    /**
     * Получает данные о количестве туров на каждый день.
     *
     * @return список объектов с датами и количеством туров
     */
    public List<Object[]> getToursPerDay() {
        return agencyRepository.getToursPerDay();
    }
}
