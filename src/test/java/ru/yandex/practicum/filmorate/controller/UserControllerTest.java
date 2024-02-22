package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void valUserEmptyAndIsBlankEmailTest() {

        User user1 = new User(1, null, "yandex", "test", LocalDate.of(2024, 1, 3), null);
        User user2 = new User(1, "", "yandex", "test", LocalDate.of(2024, 1, 3), null);
        User user3 = new User(1, " ", "yandex", "test", LocalDate.of(2024, 1, 3), null);
        User user4 = new User(1, "practicum", "yandex", "test", LocalDate.of(2024, 1, 3), null);
        User user5 = new User(1,"practicum@yandex.ru", "yandex", "test", LocalDate.of(2024, 1, 3), null);
        User user6 = User.builder()
                .email(user5.getEmail())
                .login(user5.getLogin())
                .name(user5.getName())
                .birthday(user5.getBirthday())
                .build();

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user1)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user2)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", ex.getMessage());

        final ValidationException exc = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user3)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", exc.getMessage());

        final ValidationException exce = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user4)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", exce.getMessage());

        assertEquals(user5, userController.postUserCreate(user6));
    }

    @Test
    void valUserEmptyAndIsBlankLoginTest() {

        User user1 = new User(1, "practicum@yandex.ru", null, "test", LocalDate.of(2024, 1, 3), null);
        User user2 = new User(1, "practicum@yandex.ru", "", "test", LocalDate.of(2024, 1, 3), null);
        User user3 = new User(1, "practicum@yandex.ru", " ", "test", LocalDate.of(2024, 1, 3), null);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user1)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user2)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", ex.getMessage());

        final ValidationException exc = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user3)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", exc.getMessage());
    }

    @Test
    void valUserEmptyAndIsBlankNameTest() {

        User user1 = new User(1, "practicum@yandex.ru", "yandex", null, LocalDate.of(2024, 1, 3), null);
        User user2 = new User(1, "practicum@yandex.ru", "yandex", "", LocalDate.of(2024, 1, 3), null);
        User user3 = new User(1, "practicum@yandex.ru", "yandex", " ", LocalDate.of(2024, 1, 3), null);
        User user4 = new User(1, "practicum@yandex.ru", "yandex", "yandex", LocalDate.of(2024, 1, 3), null);

        assertEquals(user4, userController.postUserCreate(user1));
        assertEquals(user4, userController.putUserCreate(user2));
        assertEquals(user4, userController.putUserCreate(user3));
    }

    @Test
    void valUserBirthdayTest() {

        User user1 = new User(1, "practicum@yandex.ru", "yandex", "test", LocalDate.of(2025, 1, 3), null);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> UserService.validationObjectUser(user1)
        );
        assertEquals("Дата рождения не может быть в будущем", e.getMessage());
    }
}
