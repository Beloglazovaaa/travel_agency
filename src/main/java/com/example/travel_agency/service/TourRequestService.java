package com.example.travel_agency.service;

import com.example.travel_agency.model.TourRequest;
import com.example.travel_agency.repository.TourRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourRequestService {

    private final TourRequestRepository tourRequestRepository;

    
    public TourRequestService(TourRequestRepository tourRequestRepository) {
        this.tourRequestRepository = tourRequestRepository;
    }

    public void save(TourRequest request) {
        tourRequestRepository.save(request);
    }

    public List<TourRequest> findAllNew() {
        return tourRequestRepository.findByStatus("Новая");
    }

    public TourRequest findById(Long id) {
        return tourRequestRepository.findById(id).orElse(null);
    }

}
