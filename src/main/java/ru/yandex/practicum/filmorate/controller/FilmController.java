package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate firstRelease = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен запрос на получение списка всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film postCreate(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление нового фильма c названием " + film.getName());
        validationFilm(film);
        film.setId(id);
        films.put(id++, film);
        log.info("Фильм " + film.getName() + " добавлен");
        return film;
    }

    @PutMapping
    public Film putCreate(@Valid @RequestBody Film film) {
        log.info("Получен запрос на изменение данных фильма " + film.getName());
        validationFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Данные фильма " + film.getName() + " изменены");
        } else throw new ValidationException("Не существует фильма с переданным ID");
        return film;
    }

    public static void validationFilm(Film film) {
        if (film.getReleaseDate().isBefore(firstRelease)) {
            log.warn("Некорректно передана дата выхода фильма");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Некорректно передано название фильма");
            throw new ValidationException("Не задано имя фильма");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Длина описания фильма > 200");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.warn("Некорректно передана продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
