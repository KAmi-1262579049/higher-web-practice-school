package ru.yandex.practicum.service;

import ru.yandex.practicum.model.*;
import java.util.*;

// Класс AuthorizationService
public class AuthorizationService {
    // Поле отображает роль пользователя на набор разрешённых прав
    private final Map<Role, Set<Permission>> permissions = new HashMap<>();

    // Метод назначения разрешения конкретной роли
    public void allow(Role role, Permission permission) {
        permissions.computeIfAbsent(role, r -> new HashSet<>()).add(permission);
    }

    // Метод для проверки, разрешено ли пользователю выполнять действие
    public boolean authorize(User user, Permission permission) {
        return permissions
                .getOrDefault(user.role, Set.of())
                .contains(permission);
    }

}
