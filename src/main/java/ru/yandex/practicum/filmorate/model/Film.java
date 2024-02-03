package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private int id;
    @NotNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
