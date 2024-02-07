package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void valFilmEmptyAndIsBlankNameTest() {

        Film film1 = new Film(1, null, "Звездные приключения", LocalDate.of(2014, 10, 26), 169);
        Film film2 = new Film(1, "", "Звездные приключения", LocalDate.of(2014, 10, 26), 169);
        Film film3 = new Film(1, " ", "Звездные приключения", LocalDate.of(2014, 10, 26), 169);
        Film film4 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(2014, 10, 26), 169);
        Film film5 = Film.builder()
                .name(film4.getName())
                .description(film4.getDescription())
                .releaseDate(film4.getReleaseDate())
                .duration(film4.getDuration())
                .build();

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film1)
        );
        assertEquals("Не задано имя фильма", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film2)
        );
        assertEquals("Не задано имя фильма", ex.getMessage());

        final ValidationException exc = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film3)
        );
        assertEquals("Не задано имя фильма", exc.getMessage());

        assertEquals(film4, filmController.postCreate(film5));
    }

    @Test
    void valFilmEmptyAndIsBlankDescriptionTest() {

        Film film1 = new Film(1, "Interstellar", null, LocalDate.of(2014, 10, 26), 169);
        Film film2 = new Film(1, "Interstellar", "Коллектив исследователей и учёных отправляется " +
                "сквозь червоточину (которая предположительно соединяет области пространства-времени через большое" +
                " расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека" +
                " и найти планету с подходящими для человечества условиями",
                LocalDate.of(2014, 10, 26), 169);

        assertEquals(film1, filmController.postCreate(film1));

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film2)
        );
        assertEquals("Максимальная длина описания — 200 символов", ex.getMessage());
    }

    @Test
    void valFilmReleaseDateTest() {

        Film film1 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(1891, 10, 26), 169);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film1)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", e.getMessage());
    }

    @Test
    void valFilmDurationTest() {

        Film film1 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(2014, 10, 26), -10);
        Film film2 = new Film(1, "Interstellar", "Звездные приключения", LocalDate.of(2014, 10, 26), 0);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film1)
        );
        assertEquals("Продолжительность фильма должна быть положительной", e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,
                () -> FilmController.validationFilm(film2)
        );
        assertEquals("Продолжительность фильма должна быть положительной", ex.getMessage());
    }
}
