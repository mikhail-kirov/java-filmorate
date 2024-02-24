package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate FIRST_RELEASE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilm(Integer id) {
        log.info("Запрос на получение данных фильма с ID " + id);
        validationFilmId(id);
        log.info("Данные фильма " + filmStorage.getFilm(id).getName() + " отправлены");
        return filmStorage.getFilm(id);
    }

    public Film postFilm(Film film) {
        log.info("Запрос на добавление фильма с названием " + film.getName());
        validationObjectFilm(film);
        log.info("Фильм " + film.getName() + " добавлен");
        return filmStorage.postFilm(film);
    }

    public Film putFilm(Film film) {
        log.info("Запрос на изменение данных фильма " + film.getName());
        validationFilmId(film.getId());
        validationObjectFilm(film);
        log.info("Данные фильма " + film.getName() + " изменены");
        return filmStorage.putFilm(film);
    }

    public Collection<Film> getAllFilm() {
        log.info("Получен запрос на получение списка всех фильмов");
        return filmStorage.getAllFilm().values();
    }

    public Film addLike(Integer filmId, Integer userId) {
        log.info("Запрос на добавление Like фильму с ID " + filmId + " от пользователя с ID " + userId);
        validationFilmId(filmId);
        validationUserId(userId);
        Film film = filmStorage.getFilm(filmId);
        if (film.getLikes() == null) {
            film.setLikes(Set.of(userId));
        } else {
            Set<Integer> filmIdList = new HashSet<>(film.getLikes());
            filmIdList.add(userId);
            film.setLikes(new HashSet<>(filmIdList));
        }
        log.info("Like от " + userStorage.getUser(userId).getLogin() + " фильму " + film.getName() + " поставлен");
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        log.info("Запрос на снятие Like от пользователя с ID " + userId + " с фильма ID " + filmId);
        validationFilmId(filmId);
        validationUserId(userId);
        Film film = filmStorage.getFilm(filmId);
        boolean isFilmInLike = film.getLikes().contains(userId);
        Set<Integer> filmLikeList = new HashSet<>(film.getLikes());
        if (isFilmInLike) {
            filmLikeList.remove(userId);
            film.setLikes(filmLikeList);
        }
        log.info("Like от " + userStorage.getUser(userId).getLogin() + " с фильма " + film.getName() + " снят");
        return film;
    }

    public Collection<Film> popularFilm(Integer count) {
        log.info("Запрос на получение списка популярных фильмов в количестве " + count);
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        Collection<Film> films = filmStorage.getAllFilm().values();
        List<Film> filmsLikesNull = filmStorage.getAllFilm().values().stream()
                .filter(film -> film.getLikes() == null || film.getLikes().isEmpty())
                .collect(Collectors.toList());
        List<Film> filmsLikesNotNull = filmStorage.getAllFilm().values().stream()
                .filter(film -> film.getLikes() != null)
                .collect(Collectors.toList());
        if (films.size() == filmsLikesNull.size()) {
            if (count > filmsLikesNull.size()) log.info("Отправлен список фильмов без Like в количестве " + filmsLikesNull.size());
            return filmsLikesNull.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }
        if (count > filmsLikesNotNull.size()) log.info("Отправлен список фильмов в количестве " + filmsLikesNull.size());
        else log.info("Отправлен список фильмов в количестве " + count);
        return filmsLikesNotNull.stream()
                .sorted(Comparator.comparingInt(Film::compareByFilm))
                .limit(count)
                .collect(Collectors.toList());
    }

    public static void validationObjectFilm(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_RELEASE)) {
            log.warn("Некорректно передана дата выхода фильма");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Некорректно передано название фильма");
            throw new ValidationException("Не задано имя фильма");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Длина описания фильма > 200. Фильм не добавлен");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.warn("Некорректно передана продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    private void validationFilmId(Integer id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            log.info("Ошибка валидации, фильм с указаным ID не найден");
            throw new FilmNotFoundException("Фильм с ID " + id + "не найден");
        }
    }

    private void validationUserId(Integer id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            log.info("Ошибка валидации, пользователь с указаным ID не найден");
            throw new UserNotFoundException("Пользователь с ID " + id + "не найден");
        }
    }
}
