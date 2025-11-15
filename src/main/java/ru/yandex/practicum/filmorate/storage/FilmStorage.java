package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public interface FilmStorage {

    @Autowired
    public final Map<Long, Film> films = new HashMap<>();

    public List<Film> getFilms();

    public Film createFilm(Film film, BindingResult br);

    public Film updateFilm(Film film, BindingResult br);

}
