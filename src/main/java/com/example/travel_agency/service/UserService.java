// UserService.java

package com.example.travel_agency.service;

import com.example.travel_agency.model.User;
import com.example.travel_agency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void save(User user) {
        // Убедимся, что роль валидна
        if (!List.of("USER", "AGENT", "ADMIN").contains(user.getRole())) {
            throw new IllegalArgumentException("Неверная роль пользователя");
        }

        // Хешируем пароль и сохраняем
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Удаление пользователя по ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }





}
