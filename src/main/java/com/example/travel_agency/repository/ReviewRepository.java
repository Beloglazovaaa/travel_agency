// ReviewRepository.java

package com.example.travel_agency.repository;

import com.example.travel_agency.model.Review;
import com.example.travel_agency.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTour(Tour tour);
}
