// Review.java

package com.example.travel_agency.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Связь с туром
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "rating", nullable = false)
    private int rating; // от 1 до 5

    @Column(name = "comment", length = 2000)
    private String comment;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    // Конструкторы
    public Review() {}

    public Review(User user, Tour tour, int rating, String comment) {
        this.user = user;
        this.tour = tour;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Tour getTour() {
        return tour;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}