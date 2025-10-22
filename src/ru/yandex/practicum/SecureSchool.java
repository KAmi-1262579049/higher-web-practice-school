package ru.yandex.practicum;

import ru.yandex.practicum.impl.SecureStateImpl;

import java.util.Map;
import java.util.Scanner;

public class SecureSchool {

    private static String currentUser;
    private static final SecureState state = new SecureStateImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String command = "X";
        do {
            showState();
            if (currentUser == null) {
                showLogin();
            } else {
                command = showMenu();
                state.doAction(currentUser, command.split(" "));
                if(command.equals("Q")){
                    currentUser = null;
                }
            }
        } while (command.equals("X"));
        showHistory();
    }

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
                            if (area > 1 && area <= 3) {
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
            System.out.println("Некорректная команда, попробуйте снова...");
        }
        return command;
    }

    private static void showLogin() {
        System.out.println("Введите логин:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();
        currentUser = state.doAction("login", login, password);
    }

    private static void showState() {
        StringBuilder stateBuilder = new StringBuilder("--===[ MAGIC SCHOOL ]===--\n");
        for(Object area: state.getAreaList()){
            stateBuilder.append(area).append("\n");
        }
        System.out.println(stateBuilder);
    }

    private static void showHistory() {
        System.out.println("-- [ DAILY HISTORY ] --");
        for(Object action:  state.getActionHistory()){
            System.out.println(action);
        }
    }

}
