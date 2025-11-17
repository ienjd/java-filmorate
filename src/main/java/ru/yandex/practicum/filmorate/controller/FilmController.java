package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/films")
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
    public Optional<Film> getFilm(@PathVariable Long id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult br) {
        return inMemoryFilmStorage.createFilm(film, br);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult br) {
        return inMemoryFilmStorage.updateFilm(film, br);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/like/{userId}")
    public Set<Long> addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLikeToFilm(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/like/{userId}")
    public Set<Long> deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public List<Film> returnMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.returnMostPopularFilms(count);
    }


}
