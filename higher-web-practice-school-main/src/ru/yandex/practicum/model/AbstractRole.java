package ru.yandex.practicum.model;

// Абстрактный класс AbstractRole
public abstract class AbstractRole implements Role {
    // Поле для хранения название роли
    private final String name;

    // Конструктор класса AbstractRole
    protected AbstractRole(String name) {
        this.name = name;
    }

    // Метод возвращает название роли пользователя
    @Override
    public String getName() {
        return name;
    }
}
