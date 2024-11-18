package com.example.travel_agency.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Роль не может быть пустой")
    @Column(nullable = false)
    private String role; // Например, "ADMIN", "AGENT", "USER"

    // Конструктор по умолчанию
    public User() {
    }

    // Конструктор с параметрами
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
