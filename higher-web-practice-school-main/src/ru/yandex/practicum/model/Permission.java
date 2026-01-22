package ru.yandex.practicum.model;

import java.util.Objects;

// Класс Permission
public class Permission {
    // Поле, определяющее тип действия которое разрешено
    public final Action action;
    // Поле, определяющее область к которой относится действие
    public final Area area;

    // Конструктор класса Permission
    public Permission(Action action, Area area) {
        this.action = action;
        this.area = area;
    }

    // Переопределение метода equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Permission)) return false;
        Permission p = (Permission) o;
        return action == p.action && area == p.area;
    }

    // Переопределение метода hashCode
    @Override
    public int hashCode() {
        return Objects.hash(action, area);
    }
}
