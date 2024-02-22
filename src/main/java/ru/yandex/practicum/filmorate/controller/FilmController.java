package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@Valid @PathVariable(value = "id") Integer id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film postFilmCreate(@Valid @RequestBody Film film) {
        return filmService.postFilm(film);
    }

    @PutMapping
    public Film putFilmCreate(@Valid @RequestBody Film film) {
        return filmService.putFilm(film);
    }

    @GetMapping
    public Collection<Film> getAllFilm() {
        return filmService.getAllFilm();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putFilmLike(@Valid @PathVariable(value = "id") Integer id,
                                   @PathVariable(value = "userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteFilmLike(@Valid @PathVariable(value = "id") Integer id,
                                      @PathVariable(value = "userId") Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilm(@Valid
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.popularFilm(count);
    }
}
