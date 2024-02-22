package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUser(@Valid @PathVariable(value = "id") Integer id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User postUserCreate(@Valid @RequestBody User user) {
        return userService.postUser(user);
    }

    @PutMapping
    public User putUserCreate(@Valid @RequestBody User user) {
        return userService.putUser(user);
    }

    @GetMapping
    public Collection<User> getAllUser() {
        return userService.getAllUser();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User putAddFriend(@Valid @PathVariable(value = "id") Integer id,
                                    @PathVariable(value = "friendId") Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@Valid @PathVariable(value = "id") Integer id,
                                    @PathVariable(value = "friendId") Integer friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@Valid @PathVariable(value = "id") Integer id) {
        return userService.getFriendsToUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@Valid @PathVariable(value = "id") Integer id,
                                                 @PathVariable(value = "otherId") Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
