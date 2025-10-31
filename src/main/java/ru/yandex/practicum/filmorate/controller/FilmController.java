package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    public FilmController() {
        this.films = new HashMap<>();
    }

    private Long createFilmId() {
        long lostId = films.size();
        return ++lostId;
    }

    @GetMapping
    public List<Film> getFilms() {
        return films.values().stream().toList();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult br) {

        if (br.hasErrors()) {
            throw new ValidationException("Ошибка валидации поля " + br.getFieldError().getField());
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка валидации поля releaseDate");
        } else {
            film.setId(createFilmId());
            films.put(film.getId(), film);
            log.info("Создан фильм " + film.getName());
            return film;
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult br) {
        Film oldFilm = films.get(film.getId());
        if (br.hasErrors()) {
            throw new ValidationException("Ошибка валидации поля " + br.getFieldError().getField());
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка валидации поля releaseDate");
        } else {
            films.put(oldFilm.getId(), film);
            log.info("Обновлён фильм " + film.getName());
            return film;
        }
    }
}
