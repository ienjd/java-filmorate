package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
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
}
