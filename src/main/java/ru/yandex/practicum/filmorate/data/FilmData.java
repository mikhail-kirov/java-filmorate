package ru.yandex.practicum.filmorate.data;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FilmData {

    private final Map<Integer, Film> films = new HashMap<>();

    public void setFilm(Film film) {
        films.put(film.getId(), film);
    }
}
