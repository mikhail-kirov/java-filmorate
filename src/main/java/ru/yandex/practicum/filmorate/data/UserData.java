package ru.yandex.practicum.filmorate.data;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UserData {

    private final Map<Integer, User> users = new HashMap<>();

    public void setUser(User user) {
        users.put(user.getId(), user);
    }
}
