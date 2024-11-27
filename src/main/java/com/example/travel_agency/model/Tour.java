package com.example.travel_agency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tours")
@Getter
@Setter
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Column(name = "tour_name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Column(name = "price_per_person", nullable = false)
    private Double pricePerPerson;

    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    @ElementCollection
    @CollectionTable(name = "tour_images", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(name = "itinerary", nullable = false, length = 2000)
    private String itinerary;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // Связь с пользователем

    public Tour() {}

    public Tour(String name, String description, String destination, LocalDate startDate, LocalDate endDate, int durationDays, Double pricePerPerson, int availableSeats, List<String> images, String itinerary, Double rating, User createdBy) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationDays = durationDays;
        this.pricePerPerson = pricePerPerson;
        this.availableSeats = availableSeats;
        this.images = images;
        this.itinerary = itinerary;
        this.rating = rating;
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", destination='" + destination + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", durationDays=" + durationDays +
                ", pricePerPerson=" + pricePerPerson +
                ", availableSeats=" + availableSeats +
                ", images=" + images +
                ", itinerary='" + itinerary + '\'' +
                ", rating=" + rating +
                ", createdBy=" + (createdBy != null ? createdBy.getUsername() : "null") +
                '}';
    }
}
