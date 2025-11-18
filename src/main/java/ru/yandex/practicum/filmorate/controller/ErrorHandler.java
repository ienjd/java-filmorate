package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.FailReleaseDateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFound(final NotFoundException e) {
        logger.error("Failed to find object: {}", e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleParameterNotValid(final ValidationException e) {
        logger.error("Failed to validate object: {}", e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleFailReleaseDate(final FailReleaseDateException e) {
        logger.error("Failed validate date: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleOtherExceptions(final MethodArgumentNotValidException e) {
        logger.error("Failed to validate object: {}", e.getMessage(), e);
        return new ErrorResponse(e.getCause().getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleOtherExceptions(final ConstraintViolationException e) {
        logger.error("Failed to validate object: {}", e.getMessage(), e);
        return new ErrorResponse(e.getCause().getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleOtherExceptions(final Throwable e) {
        logger.error("Произошла непредвиденная ошибка: {}", e.getMessage(), e);
        return new ErrorResponse(e.getCause().getMessage());
    }


}
