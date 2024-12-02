package com.example.travel_agency.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    // Геттеры и сеттеры
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    @NotBlank(message = "Роль не может быть пустой")
    @Column(nullable = false)
    private String role; // Например, "ADMIN", "AGENT", "USER"

    @Setter
    @Getter
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный email")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean isBanned; // Поле для бана

    // Конструкторы, геттеры и сеттеры
    public User() {
        // Устанавливаем роль по умолчанию в конструкторе
        this.role = "USER"; // Установить роль по умолчанию
        this.isBanned = false;
    }

    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.isBanned = false;
    }


    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
