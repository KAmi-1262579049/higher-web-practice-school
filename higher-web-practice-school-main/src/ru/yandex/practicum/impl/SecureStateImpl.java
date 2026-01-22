package ru.yandex.practicum.impl;

import ru.yandex.practicum.SecureDatabase;
import ru.yandex.practicum.SecureState;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.model.roles.*;
import ru.yandex.practicum.service.AuthService;
import ru.yandex.practicum.service.AuthorizationService;
import ru.yandex.practicum.service.SchoolService;
import java.util.*;

// Реализация интерфейса SecureState
public class SecureStateImpl implements SecureState {
    // Список всех пользователей системы
    private final List<User> users = new ArrayList<>();
    // Сервис аутентификации пользователей
    private final AuthService authService;
    // Сервис управления правами доступа и их проверки
    private final AuthorizationService authorizationService = new AuthorizationService();
    // Сервис, отвечающий за логику школы и историю событий
    private final SchoolService schoolService = new SchoolService();
    // Реестр ролей, чтобы каждая роль существовала в единственном экземпляре
    private final Map<String, Role> roleRegistry = new HashMap<>();
    // Текущий авторизованный пользователь
    private User currentUser;

    // Конструктор класса SecureStateImpl
    public SecureStateImpl() {
        for (int i = 0; i < SecureDatabase.USERS.length; i++) {

            String login = SecureDatabase.USERS[i];
            String password = SecureDatabase.PASSWORDS[i];
            String roleName = SecureDatabase.USER_ROLES[i][0];

            Role role = createRole(roleName);
            users.add(new User(login, password, role));
        }

        authService = new AuthService(users);

        for (String[] roleActions : SecureDatabase.ROLE_ACTIONS) {

            String roleName = roleActions[0];
            Role role = createRole(roleName);

            for (int i = 1; i < roleActions.length; i++) {

                String rawRule = roleActions[i];

                if (rawRule.endsWith("?")) {
                    rawRule = rawRule.substring(0, rawRule.length() - 1);
                }

                String[] parts = rawRule.split(":");
                Action action = Action.valueOf(parts[0].toUpperCase());

                if (parts.length == 1) {
                    for (Area area : Area.values()) {
                        authorizationService.allow(
                                role,
                                new Permission(action, area)
                        );
                    }
                }
                else {
                    Area area = switch (parts[1]) {
                        case "school" -> Area.SCHOOL;
                        case "room" -> Area.ROOM;
                        case "cabin" -> Area.CABIN;
                        case "class" -> Area.CLASS;
                        case "journal" -> Area.JOURNAL;
                        default -> null;
                    };

                    if (area != null) {
                        authorizationService.allow(
                                role,
                                new Permission(action, area)
                        );
                    }
                }
            }
        }
    }

    // Метод возвращает список объектов, находящихся в школе
    @Override
    public List<Object> getAreaList() {

        List<Object> result = new ArrayList<>();

        for (User user : schoolService.getUsersInSchool()) {
            String icon = SecureDatabase.USER_ICONS.get(user.login);
            result.add(icon != null ? icon : user.login);
        }

        return result;
    }

    // Метод возвращает историю действий в школе
    @Override
    public List<Object> getActionHistory() {
        return new ArrayList<>(schoolService.history());
    }

    // Метод для обработки команд пользователя
    @Override
    public String doAction(String action, String... arguments) {

        // LOGIN
        if ("login".equals(action)) {
            currentUser = authService.login(arguments[0], arguments[1]);
            return currentUser == null ? null : currentUser.login;
        }

        if (currentUser == null) {
            return null;
        }

        switch (action) {
            case "E":
                handleEnter(arguments);
                break;
            case "L":
                schoolService.leave(currentUser);
                System.out.println("Left school");
                break;
            case "J":
                handleJournal();
                break;
            case "H":
                schoolService.history().forEach(System.out::println);
                break;
            case "Q":
                currentUser = null;
                break;


        }

        return null;
    }

    // Метод обработки входа пользователя в зону
    private void handleEnter(String[] args) {

        Area area = resolveArea(args);

        if (area == null) {
            System.out.println("Wrong area");
            return;
        }

        if (currentUser.role instanceof TeacherRole && area == Area.CABIN) {
            System.out.println("Access denied");
            return;
        }

        if (currentUser.role instanceof StudentRole && area == Area.CLASS) {
            if (!schoolService.hasTeacherInSchool()) {
                System.out.println("Access denied (no teacher present)");
                return;
            }
        }

        if (!check(currentUser, Action.ENTER, area)) {
            System.out.println("Access denied");
            return;
        }

        schoolService.enter(currentUser, area);
        System.out.println(currentUser.login + " entered " + area);
    }

    // Метод для обработки команды просмотра журнала
    private void handleJournal() {

        if (!check(currentUser, Action.WATCH, Area.JOURNAL)) {
            System.out.println("Access denied");
            return;
        }

        if (currentUser.role instanceof StudentRole) {
            if (!schoolService.hasTeacherInSchool()) {
                System.out.println("Access denied (no teacher present)");
                return;
            }
            System.out.println("Journal: your grades and classmates grades");
            return;
        }

        if (currentUser.role instanceof ParentRole) {
            System.out.println("Journal: grades of Roo only");
            return;
        }

        System.out.println("Journal: full access");
    }

    // Метод для проверки наличия разрешения у пользователя
    private boolean check(User user, Action action, Area area) {
        return authorizationService.authorize(
                user,
                new Permission(action, area)
        );
    }

    // Метод для преобразования аргументов команды в зону
    private Area resolveArea(String[] args) {
        if (args.length < 2) return null;

        return switch (args[1]) {
            case "1" -> Area.SCHOOL;
            case "2" -> Area.CABIN;
            case "3" -> Area.ROOM;
            case "4" -> Area.CLASS;
            default -> null;
        };
    }

    // Метод создаёт или возвращает существующую роль
    private Role createRole(String roleName) {
        return roleRegistry.computeIfAbsent(roleName, name -> switch (name) {
            case "ADMIN" -> new AdminRole();
            case "CHIEF" -> new ChiefRole();
            case "TEACHER" -> new TeacherRole();
            case "GUARD" -> new GuardRole();
            case "STUDENT" -> new StudentRole();
            case "PARENT" -> new ParentRole();
            default -> throw new IllegalArgumentException("Unknown role: " + name);
        });
    }

    // Метод парсит строковое правило в набор разрешений
    private Set<Permission> parsePermission(String rule) {

        Set<Permission> result = new HashSet<>();

        String cleanRule = rule.replace("?", "");
        String[] parts = cleanRule.split(":");

        Action action = Action.valueOf(parts[0].toUpperCase());

        if (parts.length == 1) {
            for (Area area : Area.values()) {
                result.add(new Permission(action, area));
            }
            return result;
        }

        Area area = switch (parts[1]) {
            case "school" -> Area.SCHOOL;
            case "room" -> Area.ROOM;
            case "cabin" -> Area.CABIN;
            case "class" -> Area.CLASS;
            case "journal" -> Area.JOURNAL;
            default -> null;
        };

        if (area != null) {
            result.add(new Permission(action, area));
        }

        return result;
    }
}
