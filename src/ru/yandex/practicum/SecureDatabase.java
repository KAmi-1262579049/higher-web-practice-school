package ru.yandex.practicum;

import java.util.Map;

public class SecureDatabase {

    public static final String[] USERS = {
            "pooh", "piglet", "tigger", "eeyore", "kanga", "roo", "rabbit", "owl", "robin"
    };

    public static final Map<String, String> USER_ICONS = Map.of(
            "pooh", "🐻",
            "piglet", "🐷",
            "tigger", "🐯",
            "eeyore", "🐴",
            "kanga", "🦘",
            "roo", "🐭",
            "rabbit", "🐰",
            "owl", "🦉",
            "robin", "🧒"
    );

    public static final String[] PASSWORDS = {
            "1!", "2@",  "3#", "4$", "??", "5%", "6^", "7&", "**"
    };

    public static final String[] ROLES = {
            "ADMIN", "CHIEF", "TEACHER", "GUARD", "STUDENT", "PARENT"
    };

    public static final String[][] USER_ROLES = {
            {"GUARD"},
            {"TEACHER"},
            {"TEACHER"},
            {"TEACHER"},
            {"PARENT"},
            {"STUDENT"},
            {"TEACHER"},
            {"CHIEF"},
            {"ADMIN"}
    };

    public static final String[] ACTIONS = {
            "enter", "leave", "watch", "edit"
    };

    public static final String[] AREA = {
            "school", "room", "cabin", "class"
    };

    public static final String[] DATA = {
            "journal"
    };

    public static final String[][] ROLE_ACTIONS = {
            {"ADMIN", "enter", "leave", "watch", "edit"},
            {"CHIEF", "enter", "leave", "watch"},
            {"TEACHER", "enter:school", "leave", "enter:room", "enter:class", "enter:cabin?", "watch:journal", "edit:journal"},
            {"GUARD", "enter:school", "leave:school"},
            {"STUDENT", "enter:school", "enter:class?", "leave", "watch:journal?"},
            {"PARENT", "enter:school", "leave", "watch:journal?", "enter:cabin?"}
    };
}
