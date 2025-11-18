package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Validated
    public Film getFilm(@PathVariable @Positive Long id) {
        return inMemoryFilmStorage.getFilm(id).get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Validated
    public Film createFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping
    @Validated
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/like/{userId}")
    @Validated
    public Set<Long> addLikeToFilm(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        return filmService.addLikeToFilm(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/like/{userId}")
    @Validated
    public Set<Long> deleteLikeFromFilm(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    @Validated
    public List<Film> returnMostPopularFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        return filmService.returnMostPopularFilms(count);
    }


}
