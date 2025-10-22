package ru.yandex.practicum.impl;

import ru.yandex.practicum.SecureState;

import java.util.List;

public class SecureStateImpl implements SecureState {

    @Override
    public List<Object> getAreaList() {
        return List.of();
    }

    @Override
    public List<Object> getActionHistory() {
        return List.of();
    }

    @Override
    public String doAction(String action, String... arguments) {
        throw new RuntimeException("Not implemented");
    }
}
