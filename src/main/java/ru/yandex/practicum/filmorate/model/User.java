package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    @Setter
    private Integer id;
    @Email @NotNull
    private final String email;
    @NotNull @NotBlank
    private final String login;
    private final String name;
    private final LocalDate birthday;
    private Set<Integer> friends;
}
