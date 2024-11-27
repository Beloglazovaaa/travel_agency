package com.example.travel_agency.repository;

import com.example.travel_agency.model.TourRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRequestRepository extends JpaRepository<TourRequest, Long> {
    List<TourRequest> findByStatus(String status);
}
