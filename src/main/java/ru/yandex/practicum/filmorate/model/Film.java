package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
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
