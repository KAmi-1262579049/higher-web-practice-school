package ru.yandex.practicum;

import ru.yandex.practicum.impl.SecureStateImpl;
import java.util.Scanner;

// Класс SecureSchool
public class SecureSchool {
    // Имя текущего авторизованного пользователя
    private static String currentUser;
    // Объект состояния системы, отвечающий за доступы, действия и логику безопасности
    private static final SecureState state = new SecureStateImpl();
    // Сканер для чтения команд и данных пользователя из стандартного ввода
    private static final Scanner scanner = new Scanner(System.in);

    // Главный метод программы
    public static void main(String[] args) {
        String command = null;

        do {
            showState();

            if (currentUser == null) {
                showLogin();
            } else {
                command = showMenu();

                if (command != null) {
                    if (command.equals("Q")) {
                        currentUser = null;
                    } else {
                        String[] parts = command.split(" ");
                        state.doAction(parts[0], parts);
                    }
                }
            }

        } while (!"X".equals(command));

        showHistory();
    }

    // Метод отображает меню и валидирует команду пользователя
    private static String showMenu() {
        System.out.println("== [ Menu ] ==");
        System.out.println("E) Enter area:   \n      1) School ");
        System.out.println("     2) Owl Cabin");
        System.out.println("     3) Teachers Room");
        System.out.println("     4) Class A, B, C, or D");
        System.out.println("L) Leave area");
        System.out.println("J) Watch journal");
        System.out.println("H) Show action history");
        System.out.println("Q) Logout");
        System.out.println();
        System.out.println("X) Exit program");
        String command = scanner.nextLine();
        if (command.isBlank()) {
            command = null;
        }
        if (command != null) {
            String[] commandParts = command.toUpperCase().split(" ");
            switch (commandParts[0]) {
                case "L":
                case "J":
                case "Q":
                case "H":
                case "X":
                    if (commandParts.length > 1) {
                        System.out.println("Too many arguments");
                        command = null;
                    }
                    break;
                case "E":
                    if(commandParts.length < 2){
                        System.out.println("Too few arguments");
                        command = null;
                    } else {
                        try {
                            int area = Integer.parseInt(commandParts[1]);
                            if (area >= 1 && area <= 3) {
                                if (commandParts.length > 2) {
                                    System.out.println("Too many arguments");
                                    command = null;
                                }
                            } else if (area == 4){
                                if (commandParts.length == 3) {
                                    if(!"ABCD".contains(commandParts[2])){
                                        System.out.println("Invalid arguments");
                                        command = null;
                                    }
                                } else {
                                    System.out.println("Wrong class");
                                    command = null;
                                }
                            } else {
                                System.out.println("Wrong area");
                                command = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid area number");
                            command = null;
                        }
                    }
                    break;
                default:
                    command = null;
            }
        } else {
            System.out.println("Incorrect command, please try again...");
        }
        return command;
    }

    // Метод запрашивает логин и пароль пользователя
    private static void showLogin() {
        System.out.println("Enter your username:");
        String login = scanner.nextLine();
        System.out.println("Enter the password:");
        String password = scanner.nextLine();
        currentUser = state.doAction("login", login, password);
    }

    // Метод отображает текущее состояние школы
    private static void showState() {
        StringBuilder stateBuilder = new StringBuilder("--===[ MAGIC SCHOOL ]===--\n");
        for(Object area: state.getAreaList()){
            stateBuilder.append(area).append("\n");
        }
        System.out.println(stateBuilder);
    }

    // Метод выводит историю действий пользователя
    private static void showHistory() {
        System.out.println("-- [ DAILY HISTORY ] --");
        for(Object action:  state.getActionHistory()){
            System.out.println(action);
        }
    }

}
