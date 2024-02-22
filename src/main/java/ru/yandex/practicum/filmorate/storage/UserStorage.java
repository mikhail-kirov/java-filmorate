package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    User getUser(Integer id);
    Map<Integer, User> getAllUser();
    User postUser(User user);
    User putUser(User user);
    User removeUser(User user);
}
