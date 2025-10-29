package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@Data
public class Film {

    long id;

    String name;

    String description;

    LocalDate releaseDate;

    Long duration;

}
