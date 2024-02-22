package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(Integer id) {
        log.info("Запрос на получение данных пользователя с ID " + id);
        validationUserId(id);
        log.info("Данные пользователя " + userStorage.getUser(id).getLogin() + " отправлены");
        return userStorage.getUser(id);
    }

    public User postUser(User user) {
        log.info("Запрос на создание нового пользователя c логином " + user.getLogin());
        validationObjectUser(user);
        log.info("Пользователь с логином " + user.getLogin() + " добавлен");
        return userStorage.postUser(user);
    }

    public User putUser(User user) {
        log.info("Запрос на изменение данных пользователя " + user.getLogin());
        validationUserId(user.getId());
        validationObjectUser(user);
        log.info("Данные пользователя " + user.getLogin() + " изменены");
        return userStorage.putUser(user);
    }

    public Collection<User> getAllUser() {
        log.info("Запрос на получение списка всех пользователей");
        return userStorage.getAllUser().values();
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("Запрос на добавление пользователя с ID " + friendId + " в друзья к ID " + userId);
        validationUserId(userId);
        validationUserId(friendId);
        User user = userStorage.getUser(userId);
        User friendUser = userStorage.getUser(friendId);
        if (user.getFriends() == null) {
            user.setFriends(Set.of(friendId));
        } else {
            Set<Integer> userIdList = new HashSet<>(user.getFriends());
            userIdList.add(friendId);
            user.setFriends(new HashSet<>(userIdList));
        }
        if (friendUser.getFriends() == null) {
            friendUser.setFriends(Set.of(userId));
        } else {
            Set<Integer> friendsIdList = new HashSet<>(friendUser.getFriends());
            friendsIdList.add(userId);
            friendUser.setFriends(new HashSet<>(friendsIdList));
        }
        log.info("Пользователь " + friendUser.getLogin() + " добавлен в друзья к " + user.getLogin());
        return user;
    }

    public User removeFriend(Integer userId, Integer friendId) {
        log.info("Запрос от пользователя с ID " + friendId + " на удаление из друзей ID " + userId);
        validationUserId(userId);
        validationUserId(friendId);
        User user = userStorage.getUser(userId);
        User friendUser = userStorage.getUser(friendId);
        boolean isUserInFriends = user.getFriends().contains(friendId);
        if (isUserInFriends) {
            user.getFriends().remove(friendId);
            friendUser.getFriends().remove(userId);
        }
        log.info("Пользователь " + friendUser.getLogin() + " удален из друзей " + user.getLogin());
        return user;
    }

    public Collection<User> getFriendsToUser(Integer userId) {
        log.info("Запрос на получение списка всех друзей пользователя c ID " + userId);
        validationUserId(userId);
        Map<Integer, User> users = userStorage.getAllUser();
        Set<Integer> friendsIdList = users.get(userId).getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsIdList) {
            if (users.get(id) != null) friends.add(users.get(id));
        }
        log.info("Списка всех друзей пользователя " + users.get(userId).getLogin() + " отправлен");
        return friends;
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        log.info("Запрос на получение списка общих друзей пользователей с ID " + userId + " и " + otherId);
        User user = userStorage.getUser(userId);
        User otherIdFriend = userStorage.getUser(otherId);
        validationUserId(userId);
        validationUserId(otherId);
        log.info("Список общих друзей " + user.getLogin() + " и " + otherIdFriend.getLogin() + " отправлен");
        if (user.getFriends() == null || otherIdFriend.getFriends() == null) return List.of();
        return userStorage.getAllUser().values().stream()
                .filter(u -> user.getFriends().contains(u.getId()) && otherIdFriend.getFriends().contains(u.getId()))
                .collect(Collectors.toList());
    }

    private void validationUserId(Integer id) {
        if (id <= 0) {
            log.info("Ошибка валидации, неверно указан ID: " + id);
            throw new UserNotFoundException("Пользователь с ID " + id + "не найден");
        }
        User user = userStorage.getUser(id);
        if (user == null) {
            log.info("Ошибка валидации, неверно указан ID: " + id);
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
    }

    public static void validationObjectUser(User user) {
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
}
