package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {
    private int id;
    @Email @NotNull
    private final String email;
    @NotNull
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
