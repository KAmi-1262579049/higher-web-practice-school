package ru.yandex.practicum;

import java.util.List;

public interface SecureState {

    List<Object> getAreaList();

    List<Object> getActionHistory();

    String doAction(String action, String ...arguments);
}
