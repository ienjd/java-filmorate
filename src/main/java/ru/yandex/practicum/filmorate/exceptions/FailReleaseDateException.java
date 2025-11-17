package ru.yandex.practicum.filmorate.exceptions;

public class FailReleaseDate extends RuntimeException {
    public FailReleaseDate(String message) {
        super(message);
    }

}
