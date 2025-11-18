package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.yandex.practicum.filmorate.controller.ErrorHandler;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ControllerAdvice
public class UserControllerTest {
    private UserController controller;

    private InMemoryUserStorage inMemoryUserStorage;

    private UserService userService;

    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUpUserController() {
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService(inMemoryUserStorage);
        controller = new UserController(inMemoryUserStorage, userService);
        errorHandler = new ErrorHandler();
    }

    @Test
    public void controllerCreateUserObject() {
        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        controller.createUser(user);

        assertEquals("Vlad", controller.getUsers().getFirst().getName());
    }

    @Test
    public void controllerUpdateUserObject() {

        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        controller.createUser(user);
        assertEquals("Vlad", controller.getUsers().getFirst().getName());

        User user1 = user;
        user1.setName("NotVlad");
        controller.updateUser(user1);

        assertEquals("NotVlad", controller.getUsers().getLast().getName());
    }

    @Test
    public void withoutNameValueControllerSetLoginAsName() {

        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad_Shutov")
                .name("")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        controller.createUser(user);

        assertEquals("Vlad_Shutov", controller.getUsers().getLast().getName());
    }

    @Test
    public void withLoginEqualsNullControllerThrowsValidationException() {

        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login(null)
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void withoutAtSymbolOrWithEmptyEmailControllerThrowsValidationException() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        User user = User.builder()
                .email("coldhobbit31ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());

        user.setEmail("");

        assertFalse(violations.isEmpty());

    }

    @Test
    public void withBirthdayIsAfterCurrentDayControllerThrowsValidationException() {
        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("2026-12-31"))
                .build();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }
}
