package ru.yandex.practicum.filmorate.exceptions;

public class FailReleaseDateException extends RuntimeException {
    public FailReleaseDateException(String message) {
        super(message);
    }

}
