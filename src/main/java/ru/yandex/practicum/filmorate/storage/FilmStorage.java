package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Map;

public interface FilmStorage {
    Film getFilm(Integer id);
    Map<Integer, Film> getAllFilm();
    Film postFilm(Film film);
    Film putFilm(Film film);
    Film removeFilm(Integer id);
}
