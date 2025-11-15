package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Builder
@Data
public class Film {

    private Long id;

    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;


    private LocalDate releaseDate;

    @Positive
    private Long duration;

    private Set<Long> likes;

}
