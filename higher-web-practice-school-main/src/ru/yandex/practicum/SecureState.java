package ru.yandex.practicum;

import java.util.List;

// Интерфейс SecureState описывает контракт для безопасного состояния системы
public interface SecureState {
    // Метод возвращает список объектов, представляющих доступные области или зоны
    List<Object> getAreaList();
    // Метод возвращает список объектов, описывающих историю выполненных действий
    List<Object> getActionHistory();
    // Метод выполняет действие по его названию с произвольным набором аргументов и возвращает результат в виде строки
    String doAction(String action, String ...arguments);
}
