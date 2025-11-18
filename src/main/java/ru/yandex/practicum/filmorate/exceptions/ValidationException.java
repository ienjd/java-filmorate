package ru.yandex.practicum.filmorate.exceptions;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
