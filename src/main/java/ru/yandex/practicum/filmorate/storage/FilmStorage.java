package ru.yandex.practicum.filmorate.storage;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {


    public List<Film> getFilms();

    public Film createFilm(Film film, BindingResult br);

    public Film updateFilm(Film film, BindingResult br);

}
