package ru.yandex.practicum.service;

import ru.yandex.practicum.model.User;
import java.util.List;

// Класс AuthService
public class AuthService {
    // Поле users - список всех зарегистрированных пользователей системы
    private final List<User> users;

    // Конструктор класса AuthService
    public AuthService(List<User> users) {
        this.users = users;
    }

    // Метод выполняет авторизацию пользователя
    public User login(String login, String password) {
        return users.stream()
                .filter(u -> u.login.equals(login) && u.password.equals(password))
                .findFirst()
                .orElse(null);
    }
}
