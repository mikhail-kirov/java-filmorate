package ru.yandex.practicum.filmorate.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));

    @Test
    void valFilmEmptyAndIsBlankNameTest() {

        Film film1 = new Film(1, null, "Звездные приключения", LocalDate.of(2014, 10, 26), 169, null, Genre.Action, Rating.R);
        Film film2 = new Film(1, "", "Звездные приключения", LocalDate.of(2014, 10, 26), 169, null, Genre.Action, Rating.R);
        Film film3 = new Film(1, " ", "Звездные приключения", LocalDate.of(2014, 10, 26), 169, null, Genre.Cartoon, Rating.PG);
        Film film4 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(2014, 10, 26), 169, null, Genre.Action, Rating.R);
        Film film5 = Film.builder()
                .name(film4.getName())
                .description(film4.getDescription())
                .releaseDate(film4.getReleaseDate())
                .duration(film4.getDuration())
                .genre(Genre.Action)
                .rating(Rating.R)
                .build();

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film1)
        );
        assertEquals("Не задано имя фильма", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film2)
        );
        assertEquals("Не задано имя фильма", ex.getMessage());

        final ValidationException exc = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film3)
        );
        assertEquals("Не задано имя фильма", exc.getMessage());

        assertEquals(film4, filmController.postFilmCreate(film5), "Фильм не сохранён");
    }

    @Test
    void valFilmEmptyAndIsBlankDescriptionTest() {

        Film film1 = new Film(1, "Interstellar", null, LocalDate.of(2014, 10, 26), 169, null, Genre.Action, Rating.R);
        Film film2 = new Film(1, "Interstellar", "Коллектив исследователей и учёных отправляется " +
                "сквозь червоточину (которая предположительно соединяет области пространства-времени через большое" +
                " расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека" +
                " и найти планету с подходящими для человечества условиями",
                LocalDate.of(2014, 10, 26), 169, null, Genre.Action, Rating.R);

        assertEquals(film1, filmController.postFilmCreate(film1));

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film2)
        );
        assertEquals("Максимальная длина описания — 200 символов", ex.getMessage());
    }

    @Test
    void valFilmReleaseDateTest() {

        Film film1 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(1891, 10, 26), 169, null, Genre.Action, Rating.R);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film1)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", e.getMessage());
    }

    @Test
    void valFilmDurationTest() {

        Film film1 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(2014, 10, 26), -10, null, Genre.Action, Rating.R);
        Film film2 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(2014, 10, 26), 0, null, Genre.Action, Rating.PG);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film1)
        );
        assertEquals("Продолжительность фильма должна быть положительной", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> FilmService.validationObjectFilm(film2)
        );
        assertEquals("Продолжительность фильма должна быть положительной", ex.getMessage());
    }

    @Test
    void comparePopularFilmTest() {
        Film film1 = new Film(1, "Звездные войны", "Звездные приключения", LocalDate.of(1986, 10, 26), 169, Set.of(1,54,322), Genre.Action, Rating.R);
        Film film2 = new Film(2, "Доктор Кто", "Звездные приключения", LocalDate.of(1956, 10, 26), 169, Set.of(1,54,322,58), Genre.Action, Rating.R);
        Film film3 = new Film(3, "Старжи галактики", "Звездные приключения", LocalDate.of(2011, 10, 26), 169, Set.of(1,54), Genre.Action, Rating.R);
        Film film4 = new Film(4, "Интерсталлар", "Звездные приключения", LocalDate.of(2014, 10, 26), 169, Set.of(1,54,322,45,34,76,3456), Genre.Action, Rating.R);
        Film film5 = new Film(5, "Звездный путь", "Звездные приключения", LocalDate.of(2010, 10, 26), 169, Set.of(1,54,542), Genre.Action, Rating.R);
        filmController.postFilmCreate(film1);
        filmController.postFilmCreate(film2);
        filmController.postFilmCreate(film3);
        filmController.postFilmCreate(film4);
        filmController.postFilmCreate(film5);

        Collection<Film> films = List.of(film4,film2,film1,film5);
        Collection<Film> methodFilms = filmController.popularFilm(4);

        assertTrue(CollectionUtils.isEqualCollection(films, methodFilms), "Неверная сортировка по популярности фильмов");
    }
}
