package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    private UserController controller;

    @BeforeEach
    public void setUpUserController() {
        controller = new UserController();
    }

    @Test
    public void controllerCreateUserObject() {

        controller.createUser(User.builder()
                .email("coldhobbit31@ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build());

        assertEquals("Vlad", controller.getUsers().getFirst().getName());
    }

    @Test
    public void controllerUpdateUserObject() {
        controller.createUser(User.builder()
                .email("coldhobbit31@ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build());

        assertEquals("Vlad", controller.getUsers().getFirst().getName());

        controller.updateUser(User.builder()
                .id(1)
                .email("coldhobbit31@ya.ru")
                .name("NotVlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build());
        assertEquals("NotVlad", controller.getUsers().getFirst().getName());
    }

    @Test
    public void withoutNameValueControllerSetLoginAsName() {

        controller.createUser(User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build());

        assertEquals("Vlad_Shutov", controller.getUsers().getFirst().getName());
    }

    @Test
    public void withLoginEqualsNullControllerThrowsValidationException() {

        assertThrows(ValidationException.class, () -> controller.createUser(User.builder()
                .email("coldhobbit31@ya.ru")
                .login(null)
                .birthday(LocalDate.parse("1990-12-31"))
                .build()));
    }

    @Test
    public void withoutAtSymbolOrWithEmptyEmailControllerThrowsValidationException() {
        assertThrows(ValidationException.class, () -> controller.createUser(User.builder()
                .email("coldhobbit31ya.ru")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build()));

        assertThrows(ValidationException.class, () -> controller.createUser(User.builder()
                .email("")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build()));
    }

    @Test
    public void withSpaceInLoginControllerThrowsValidationException() {
        assertThrows(ValidationException.class, () -> controller.createUser(User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build()));
    }

    @Test
    public void withBirthdayIsAfterCurrentDayControllerThrowsValidationException() {
        assertThrows(ValidationException.class, () -> controller.createUser(User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("2026-12-31"))
                .build()));
    }
}
