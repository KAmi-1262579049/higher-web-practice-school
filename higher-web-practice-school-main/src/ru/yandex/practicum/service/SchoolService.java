package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Area;
import ru.yandex.practicum.model.User;
import java.util.*;

// Класс SchoolService
public class SchoolService {
    // Текущая локация каждого пользователя в школе
    private final Map<User, Area> userLocations = new HashMap<>();
    // Множество пользователей, которые в данный момент находятся в школе
    private final Set<User> usersInSchool = new HashSet<>();
    // Список строк с историей событий
    private final List<String> history = new ArrayList<>();

    // Метод, обрабатывающий вход пользователя в школу и фиксацию его локации
    public void enter(User user, Area area) {
        if (usersInSchool.add(user)) {
            history.add(user.login + " entered " + area);
        }
    }

    // Метод, обрабатывающий выход пользователя из школы
    public void leave(User user) {
        if (usersInSchool.remove(user)) {
            userLocations.remove(user);
            history.add(user.login + " left school");
        }
    }

    // Метод, возвращающий всех пользователей, находящихся в школе
    public Set<User> getUsersInSchool() {
        return Collections.unmodifiableSet(usersInSchool);
    }

    // Метод, возвращающий историю событий в школе
    public List<String> history() {
        return Collections.unmodifiableList(history);
    }

    // Метод, проверяющий, есть ли в школе хотя бы один учитель
    public boolean hasTeacherInSchool() {
        return usersInSchool.stream()
                .anyMatch(u -> u.role instanceof ru.yandex.practicum.model.roles.TeacherRole);
    }
}
