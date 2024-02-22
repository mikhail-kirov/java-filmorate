package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final UserData userData = new UserData();
    private int id = 1;

    @Override
    public User getUser(Integer id) {
        return userData.getUsers().get(id);
    }

    @Override
    public Map<Integer, User> getAllUser() {
        return userData.getUsers();
    }

    @Override
    public User postUser(User user) {
        user.setId(id++);
        if (user.getName() == null || user.getName().isBlank()) {
            userData.setUser(userNameIsBlack(user));
        } else  userData.setUser(user);
        return userData.getUsers().get(id - 1);
    }

    @Override
    public User putUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            userData.setUser(userNameIsBlack(user));
        } else userData.setUser(user);
        return userData.getUsers().get(user.getId());
    }

    @Override
    public User removeUser(User user) {
        userData.getUsers().remove(user.getId());
        log.info("Пользователь " + user.getLogin() + " удален");
        return user;
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
