package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    private FilmController controller;

    @BeforeEach
    public void setUpUserController() {
        controller = new FilmController();
    }

    @Test
    public void controllerCreateFilmObject() {
        Film film = Film.builder()
                .description("Guy Stuart Ritchie")
                .name("Snatch")
                .releaseDate(LocalDate.parse("2000-09-01"))
                .duration(Long.valueOf(104))
                .build();

        BindingResult br = new BeanPropertyBindingResult(film, "user");

        controller.createFilm(film, br);

        assertEquals("Snatch", controller.getFilms().getFirst().getName());
    }

    @Test
    public void controllerUpdateFilmObject() {
        Film film = Film.builder()
                .description("Guy Stuart Ritchie")
                .name("Snatch")
                .releaseDate(LocalDate.parse("2000-09-01"))
                .duration(Long.valueOf(104))
                .build();

        BindingResult br = new BeanPropertyBindingResult(film, "film");
        controller.createFilm(film, br);
        assertEquals(104, controller.getFilms().getFirst().getDuration());


        Film film1 = film;
        film1.setDuration(Long.valueOf(102));
        BindingResult br1 = new BeanPropertyBindingResult(film1, "film1");
        controller.updateFilm(film1, br1);

        assertEquals(102, controller.getFilms().getFirst().getDuration());
    }

    @Test
    public void withNameEqualsNullControllerThrowsValidationException() {

        Film film = Film.builder()
                .description("Guy Stuart Ritchie")
                .name("")
                .releaseDate(LocalDate.parse("2000-09-01"))
                .duration(Long.valueOf(104))
                .build();


        BindingResult br = new BeanPropertyBindingResult(film, "film");
        br.addError(new FieldError("film", "name", "Ошибка валидации поля name"));

        assertThrows(ValidationException.class, () -> controller.createFilm(film, br));
    }

    @Test
    public void withSizeOfDescriptionOver200SymbolsControllerThrowsValidationException() {
        Film film = Film.builder()
                .description("В Антверпене банда грабителей, переодетых в религиозных евреев, " +
                        "один из которых — Фрэнки (Бенисио дель Торо), похищает из еврейской " +
                        "ювелирной конторы множество драгоценностей, среди которых бриллиант в 86 карат. " +
                        "Фрэнки предстоит доставить это сокровище в пристёгиваемом к руке бронированном " +
                        "дипломате с кодовой защитой прикрывающему эту банду ювелиру Ави Деновицу " +
                        "(Дэннис Фарина) в Нью-Йорк, транзитом через Лондон. Перед отъездом один из " +
                        "партнёров Фрэнки по ограблению дает ему телефон Бориса «Бритвы» (Раде Шербеджия) — " +
                        "русского торговца оружием в Лондоне, и говорит, что если Фрэнки понадобится пушка, " +
                        "Борис к его услугам. Этот самый партнёр оказывается братом Бориса и проводит обходной " +
                        "манёвр: звонит Борису в Лондон и рассказывает о Фрэнки и бриллианте, предлагая брату " +
                        "нанять людей для кражи камня. Тем временем Ави согласовывает с Фрэнки " +
                        "его дальнейшие действия и рекомендует Дага «Голову» Деновица (Майк Рэйд[англ.]) " +
                        "— родственника-ювелира в Лондоне, который может купить мелкие бриллианты.")
                .name("Snatch")
                .releaseDate(LocalDate.parse("2000-09-01"))
                .duration(Long.valueOf(104))
                .build();

        BindingResult br = new BeanPropertyBindingResult(film, "film");
        br.addError(new FieldError("film", "description", "Ошибка валидации поля description"));

        assertThrows(ValidationException.class, () -> controller.createFilm(film, br));
    }

    @Test
    public void withNegativeDurationControllerThrowsValidationException() {

        Film film = Film.builder()
                .description("Guy Stuart Ritchie")
                .name("Snatch")
                .releaseDate(LocalDate.parse("2000-09-01"))
                .duration(Long.valueOf(-1))
                .build();


        BindingResult br = new BeanPropertyBindingResult(film, "film");
        br.addError(new FieldError("film", "duration", "Ошибка валидации поля duration"));

        assertThrows(ValidationException.class, () -> controller.createFilm(film, br));
    }

    @Test
    public void withDateOfReleaseBefore28December1895ControllerThrowsValidationException() {
        Film film = Film.builder()
                .description("Louis Aimé Augustin Le Prince")
                .name("Roundhay Garden Scene")
                .releaseDate(LocalDate.parse("1888-10-14"))
                .duration(Long.valueOf(1))
                .build();


        BindingResult br = new BeanPropertyBindingResult(film, "film");
        br.addError(new FieldError("film", "releaseDate", "Ошибка валидации поля name"));

        assertThrows(ValidationException.class, () -> controller.createFilm(film, br));
    }
}
