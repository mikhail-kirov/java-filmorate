package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;*/

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    /*private final HttpClient client = HttpClient.newHttpClient();

    private final URI url = URI.create("http://localhost:8080/users");*/

    UserController userController = new UserController();

    @Test
    void valUserEmptyAndIsBlankEmailTest() {

        User user1 = new User(1, null, "yandex", "test", LocalDate.of(2024, 1, 3));
        User user2 = new User(1, "", "yandex", "test", LocalDate.of(2024, 1, 3));
        User user3 = new User(1, " ", "yandex", "test", LocalDate.of(2024, 1, 3));
        User user4 = new User(1, "practicum", "yandex", "test", LocalDate.of(2024, 1, 3));
        User user5 = new User(1,"practicum@yandex.ru", "yandex", "test", LocalDate.of(2024, 1, 3));
        User user6 = User.builder()
                .email(user5.getEmail())
                .login(user5.getLogin())
                .name(user5.getName())
                .birthday(user5.getBirthday())
                .build();

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user1)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user2)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", ex.getMessage());

        final ValidationException exc = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user3)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", exc.getMessage());

        final ValidationException exce = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user4)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'", exce.getMessage());

        assertEquals(user5, userController.postCreate(user6));
    }

    @Test
    void valUserEmptyAndIsBlankLoginTest() {

        User user1 = new User(1, "practicum@yandex.ru", null, "test", LocalDate.of(2024, 1, 3));
        User user2 = new User(1, "practicum@yandex.ru", "", "test", LocalDate.of(2024, 1, 3));
        User user3 = new User(1, "practicum@yandex.ru", " ", "test", LocalDate.of(2024, 1, 3));

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user1)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user2)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", ex.getMessage());

        final ValidationException exc = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user3)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", exc.getMessage());
    }

    @Test
    void valUserEmptyAndIsBlankNameTest() {

        User user1 = new User(1, "practicum@yandex.ru", "yandex", null, LocalDate.of(2024, 1, 3));
        User user2 = new User(1, "practicum@yandex.ru", "yandex", "", LocalDate.of(2024, 1, 3));
        User user3 = new User(1, "practicum@yandex.ru", "yandex", " ", LocalDate.of(2024, 1, 3));
        User user4 = new User(1, "practicum@yandex.ru", "yandex", "yandex", LocalDate.of(2024, 1, 3));

        assertEquals(user4, userController.postCreate(user1));
        assertEquals(user4, userController.putCreate(user2));
        assertEquals(user4, userController.putCreate(user3));
    }

    @Test
    void valUserBirthdayTest() {

        User user1 = new User(1, "practicum@yandex.ru", "yandex", "test", LocalDate.of(2025, 1, 3));

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> UserController.validationUser(user1)
        );
        assertEquals("Дата рождения не может быть в будущем", e.getMessage());
    }

    /* при запущенном FilmorateApplication
    @Test
    void valUserEmptyRequestTest() {
        int status = 0;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
        assertEquals(400, status);
    }*/
}
