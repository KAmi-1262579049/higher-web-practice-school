package ru.yandex.practicum.model;

// Класс User
public class User {
    // Поле для хранения логина пользователя
    public final String login;
    // Поле для хранения пароля пользователя
    public final String password;
    // Поле для хранения роли пользователя
    public final Role role;

    // Конструктор класса User
    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // Переопределение метода toString
    @Override
    public String toString() {
        return login;
    }
}
