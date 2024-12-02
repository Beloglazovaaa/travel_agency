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
        if (!List.of("USER", "AGENT", "ADMIN").contains(user.getRole())) {
            throw new IllegalArgumentException("Неверная роль пользователя");
        }
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


    // Обновление роли пользователя
    public void updateUserRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setRole(role);  // Обновляем роль
        userRepository.save(user);  // Сохраняем изменения в базе данных
    }

    // Другие методы (например, для блокировки/разблокировки)
    public void toggleBan(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setBanned(!user.isBanned());  // Переключаем статус бана
        userRepository.save(user);
    }
}
