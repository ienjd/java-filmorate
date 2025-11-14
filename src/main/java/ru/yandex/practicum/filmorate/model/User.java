package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {

    private Long id;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotEmpty
    @NotBlank
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    private Set<Long> friends;
}
