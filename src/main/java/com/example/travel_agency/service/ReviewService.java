// ReviewService.java

package com.example.travel_agency.service;

import com.example.travel_agency.model.Review;
import com.example.travel_agency.model.Tour;
import com.example.travel_agency.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByTour(Tour tour) {
        return reviewRepository.findByTour(tour);
    }
}
