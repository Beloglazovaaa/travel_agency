package com.example.travel_agency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tour_requests")
@Getter
@Setter
public class TourRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false)
    private Tour tour; // связь с туром

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user; // связь с пользователем (если это актуально)

    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String userEmail;
    @Getter
    @Setter
    private String userPhone;
    @Getter
    @Setter
    private String status; // "Новая", "Обработана"

    // Конструкторы
    public TourRequest() {}

    public TourRequest(Tour tour, User user, String userName, String userEmail, String userPhone, String status) {
        this.tour = tour;
        this.user = user;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.status = status;
    }

    public Long getTourId() {
        return tour.getId();
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getTourName() {
        return tour.getName();
    }

    public void setTourName(String tourName) {
        this.tour.setName(tourName);// Геттеры и Сеттеры (сгенерированы Lombok)
    }

    public void setTourId(Long tourId) {
        if (this.tour == null) {
            this.tour = new Tour();
        }
        this.tour.setId(tourId);
    }


    public void setUserId(String userId) {
        this.user.setId(Long.parseLong(userId));
    }

    public String toString() {
        return "TourRequest{" +
                "id=" + id +
                ", tour=" + tour +
                ", user=" + user +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
