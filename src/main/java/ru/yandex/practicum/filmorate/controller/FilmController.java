package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    private Long createUserId() {
        long lostId = films.size();
        return ++lostId;
    }

    public boolean nameIsCorrect(Film film) {
        boolean nameIsCorrect = true;
        if (film.getName().isBlank()) {
            nameIsCorrect = false;
        }
        return nameIsCorrect;
    }

    public boolean descriptionIsCorrect(Film film) {
        boolean descriptionIsCorrect = true;
        if (film.getDescription().length() > 200) {
            descriptionIsCorrect = false;
        }
        return descriptionIsCorrect;
    }

    public boolean releaseDateIsCorrect(Film film) {
        boolean releaseDateIsCorrect = true;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            releaseDateIsCorrect = false;
        }
        return releaseDateIsCorrect;
    }

    public boolean durationIsPositive(Film film) {
        boolean durationIsPositive = true;

        if (film.getDuration() < 0) {
            durationIsPositive = false;
        }
        return durationIsPositive;
    }

    @GetMapping
    public List<Film> getFilms() {
        return films.values().stream().toList();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {

        if (
                (film.getName() == null) ||
                        (film.getDescription() == null) ||
                        (film.getDuration() == null) ||
                        (film.getReleaseDate() == null)) {
            throw new ValidationException("Обязательное поле должно быть заполнено");
        }

        if (film.getName() != null) {
            if (!nameIsCorrect(film)) {
                throw new ValidationException("Название фильма не может быть пустым");
            }
        }

        if (film.getDescription() != null) {
            if (!descriptionIsCorrect(film)) {
                throw new ValidationException("Описание фильма не может составлять более 200 символов");
            }
        }

        if (film.getDuration() != null) {
            if (!durationIsPositive(film)) {
                throw new ValidationException("Длительность фильма не может быть отрицательной");
            }
        }

        if (film.getReleaseDate() != null) {
            if (!releaseDateIsCorrect(film)) {
                throw new ValidationException("Дата выхода фильма не может быть ранее 28 декабря 1895");
            }
        }

        film.setId(createUserId());
        films.put(film.getId(), film);
        log.info("Создан фильм " + film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {

        Film oldFilm = films.get(film.getId());

        if (film.getName() != null) {
            if (!nameIsCorrect(film)) {
                throw new ValidationException("Название фильма не может быть пустым");
            }
            oldFilm.setName(film.getName());
        }


        if (film.getDescription() != null) {
            if (!descriptionIsCorrect(film)) {
                throw new ValidationException("Описание фильма не может составлять более 200 символов");
            }
            oldFilm.setDescription(film.getDescription());
        }

        if (film.getDuration() != null) {
            if (!durationIsPositive(film)) {
                throw new ValidationException("Длительность фильма не может быть отрицательной");
            }
            oldFilm.setDuration(film.getDuration());
        }


        if (film.getReleaseDate() != null) {
            if (!releaseDateIsCorrect(film)) {
                throw new ValidationException("Дата выхода фильма не может быть ранее 28 декабря 1895");
            }
            oldFilm.setReleaseDate(film.getReleaseDate());
        }

        log.info("Обновлён фильм " + oldFilm.getName());
        return oldFilm;
    }
}
