package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    Map <Long, Film> films;

    public InMemoryFilmStorage(Map<Long, Film> films) {
        this.films = films;
    }

    private Long createFilmId() {
        long lostId = films.size();
        return ++lostId;
    }

    public List<Film> getFilms() {
        return films.values().stream().toList();
    }

    public Film createFilm(Film film, BindingResult br) {

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

    public Film updateFilm(Film film, BindingResult br) {
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
