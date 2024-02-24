package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmData filmData = new FilmData();
    private int id = 1;

    @Override
    public Film getFilm(Integer id) {
        return filmData.getFilms().get(id);
    }

    @Override
    public Map<Integer, Film> getAllFilm() {
        return filmData.getFilms();
    }

    @Override
    public Film postFilm(Film film) {
        film.setId(id++);
        filmData.setFilm(film);
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        filmData.setFilm(film);
        return film;
    }

    @Override
    public Film removeFilm(Integer id) {
        Film film = filmData.getFilms().get(id);
        filmData.getFilms().remove(id);
        log.info("Фильм " + film.getName() + " удален");
        return film;
    }
}
