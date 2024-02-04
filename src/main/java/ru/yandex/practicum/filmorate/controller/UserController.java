package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен запрос на получение списка всех пользователей");
        return users.values();
    }

    @PostMapping
    public User postCreate(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя c логином " + user.getLogin());
        validationUser(user);
        user.setId(id);
        if (user.getName() == null || user.getName().isBlank()) {
            users.put(id++, userNameIsBlack(user));
        } else users.put(id++, user);
        log.info("Пользователь с именем " + user.getLogin() + " добавлен");
        return users.get(id - 1);
    }

    @PutMapping
    public User putCreate(@Valid @RequestBody User user) {
        log.info("Получен запрос на изменение данных пользователя " + user.getName());
        validationUser(user);
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                users.put(user.getId(), userNameIsBlack(user));
            } else users.put(user.getId(), user);
            log.info("Данные пользователя " + user.getName() + " изменены");
        } else throw new ValidationException("Не существует пользователя с переданным ID");
        return users.get(user.getId());
    }

    public static void validationUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Некорректно передан адрес электронной почты");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Некорректно передан логин пользователя");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректно передана дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private User userNameIsBlack(User user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getLogin())
                .birthday(user.getBirthday())
                .build();
    }
}
