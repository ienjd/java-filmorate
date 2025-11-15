package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private List<Film> findFilm(Long id) {
        return filmStorage.getFilms().stream()
                .filter(film -> film.getId().equals(id))
                .collect(Collectors.toList());
    }

    public Set<Long> addLikeToFilm(Long filmId, Long userId){
        findFilm(filmId).getFirst().getLikes().add(userId);
        return findFilm(filmId).getFirst().getLikes();
    }

    public void deleteLikeFromFilm(Long filmId, Long userId){
        findFilm(filmId).getFirst().getLikes().remove(userId);
    }

    public List<Film> returnMostPopularFilms(Integer count){
        System.out.println("Запрос получен");
        List <Film> sortedFilms =
                filmStorage.getFilms().stream()
                        .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                        .collect(Collectors.toList());
        return sortedFilms.subList(0, count).reversed();
    }

}
