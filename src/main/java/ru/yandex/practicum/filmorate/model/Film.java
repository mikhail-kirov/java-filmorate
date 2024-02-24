package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotNull @NotBlank
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private Set<Integer> likes;

    public int compareByFilm() {
        return -likes.size();
    }
}
