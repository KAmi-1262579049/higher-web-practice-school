package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// Тестовый класс SecureSchoolTest
class SecureSchoolTest {
    // Текущее состояние системы безопасности
    private SecureState state;

    // Метод инициализации перед каждым тестом
    @BeforeEach
    void setUp() {
        state = new SecureStateImpl();
    }

    // Тест (корректный логин должен быть успешным)
    @Test
    void loginWithCorrectCredentials_shouldSucceed() {
        String user = state.doAction("login", "pooh", "1!");
        assertEquals("pooh", user);
    }

    // Тест (логин с неправильным паролем должен завершиться неудачей)
    @Test
    void loginWithWrongPassword_shouldFail() {
        String user = state.doAction("login", "pooh", "wrong");
        assertNull(user);
    }

    // Вспомогательный метод для выполнения команд от имени пользователя
    private void command(String user, String... cmd) {
        state.doAction(user, cmd);
    }

    // Тест (охранник может войти в школу, но не может войти в кабинет директора)
    @Test
    void guardCanEnterSchoolButNotCabin() {
        String user = state.doAction("login", "pooh", "1!");
        assertEquals("pooh", user);

        int historyBefore = state.getActionHistory().size();

        command(user, "E", "1");

        command(user, "E", "2");

        assertEquals(historyBefore, state.getActionHistory().size());
    }

    // Тест (учитель может входить в учительскую и класс, но не в кабинет директора)
    @Test
    void teacherCanEnterRoomAndClassButNotCabin() {
        String user = state.doAction("login", "piglet", "2@");
        assertEquals("piglet", user);

        int historyBefore = state.getActionHistory().size();

        command(user, "E", "1");
        command(user, "E", "3");
        command(user, "E", "4", "A");

        int historyAfterAllowed = state.getActionHistory().size();

        command(user, "E", "2");

        assertEquals(historyAfterAllowed, state.getActionHistory().size());
    }

    // Тест (ученик не может войти в класс без присутствия учителя)
    @Test
    void studentCannotEnterClassWithoutTeacher() {
        String user = state.doAction("login", "roo", "5%");
        assertEquals("roo", user);

        int historyBefore = state.getActionHistory().size();

        command(user, "E", "1");

        command(user, "E", "4", "A");

        assertEquals(historyBefore, state.getActionHistory().size());
    }

    // Тест (ученик может войти в класс, если учитель уже в школе)
    @Test
    void studentCanEnterClassWhenTeacherIsInSchool() {
        state.doAction("login", "piglet", "2!");
        String teacher = "piglet";

        command(teacher, "E", "1");
        command(teacher, "Q");

        state.doAction("login", "roo", "5%");
        String student = "roo";

        command(student, "E", "1");

        int historyBefore = state.getActionHistory().size();

        command(student, "E", "4", "A");

        assertEquals(historyBefore, state.getActionHistory().size());
    }

    // Тест (родитель может войти в школу, но не может войти в класс)
    @Test
    void parentCannotEnterClassButCanEnterSchool() {
        state.doAction("login", "kanga", "??");
        String parent = "kanga";

        command(parent, "E", "1");

        int historyBefore = state.getActionHistory().size();

        command(parent, "E", "4", "A");

        assertEquals(historyBefore, state.getActionHistory().size());
    }

    // Тест (logout не удаляет пользователя из школы)
    @Test
    void logoutDoesNotRemoveUserFromSchool() {
        state.doAction("login", "piglet", "2!");
        String user = "piglet";

        command(user, "E", "1");

        int historyBeforeLogout = state.getActionHistory().size();

        command(user, "Q");

        assertEquals(historyBeforeLogout, state.getActionHistory().size());
    }

    // Тест (leave полностью удаляет пользователя из школы)
    @Test
    void leaveRemovesUserFromSchool() {
        state.doAction("login", "piglet", "2!");
        state.doAction("E", "1");

        state.doAction("L");

        List<Object> area = state.getAreaList();
        assertTrue(area.isEmpty());
    }
}
