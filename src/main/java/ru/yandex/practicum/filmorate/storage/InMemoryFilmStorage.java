package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FailReleaseDateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Long createFilmId() {
        long lostId = films.size();
        return ++lostId;
    }

    public List<Film> getFilms() {
        return films.values().stream().toList();
    }

    public Optional<Film> getFilm(Long id) {
        return Optional.of(films.values().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException("Фильм с данным id не найден")));

    }

    public Film createFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FailReleaseDateException("Некорректная дата");
        }
        try {
            film.setId(createFilmId());
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            log.info("Создан фильм " + film.getName());
            return film;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public Film updateFilm(Film film) {
        Optional<Film> oldFilm = getFilm(film.getId());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FailReleaseDateException("Ошибка валидации даты");
        }
        try {
            film.setLikes(oldFilm.get().getLikes());
            films.put(oldFilm.get().getId(), film);
            log.info("Обновлён фильм " + film.getName());
            return film;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
