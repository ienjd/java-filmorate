package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserService userService;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    private Optional<Film> findFilm(Long id) {
        return Optional.ofNullable(filmStorage.getFilms().stream()
                .filter(film -> film.getId().equals(id))
                .findAny().orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    public Set<Long> addLikeToFilm(Long filmId, Long userId) {
        userService.findFriend(userId);
        findFilm(filmId).get().getLikes().add(userId);
        return findFilm(filmId).get().getLikes();
    }

    public Set<Long> deleteLikeFromFilm(Long filmId, Long userId) {
        userService.findFriend(userId);
        findFilm(filmId).get().getLikes().remove(userId);
        return findFilm(filmId).get().getLikes();
    }

    public List<Film> returnMostPopularFilms(int count) {

        Comparator<Film> comparator = new Comparator<>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o1.getLikes().size() - o2.getLikes().size();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        return filmStorage.getFilms().stream()
                .sorted((film1, film2) -> comparator.compare(film2, film1))
                .limit(count)
                .toList();
    }
}
