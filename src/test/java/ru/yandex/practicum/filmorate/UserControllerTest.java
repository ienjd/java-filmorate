package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

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
        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        BindingResult br = new BeanPropertyBindingResult(user, "user");

        controller.createUser(user, br);

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

        BindingResult br = new BeanPropertyBindingResult(user, "user");
        controller.createUser(user, br);
        assertEquals("Vlad", controller.getUsers().getFirst().getName());

        User user1 = user;
        user1.setName("NotVlad");
        BindingResult br1 = new BeanPropertyBindingResult(user1, "user1");
        controller.updateUser(user1, br1);

        assertEquals("NotVlad", controller.getUsers().getFirst().getName());
    }

    @Test
    public void withoutNameValueControllerSetLoginAsName() {

        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad_Shutov")
                .name("")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();
        BindingResult br = new BeanPropertyBindingResult(user, "user");
        controller.createUser(user, br);

        assertEquals("Vlad_Shutov", controller.getUsers().getFirst().getName());
    }

    @Test
    public void withLoginEqualsNullControllerThrowsValidationException() {

        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login(null)
                .birthday(LocalDate.parse("1990-12-31"))
                .build();
        BindingResult br = new BeanPropertyBindingResult(user, "user");
        br.addError(new FieldError("user", "login", "Ошибка валидации поля login"));

        assertThrows(ValidationException.class, () -> controller.createUser(user, br));
    }

    @Test
    public void withoutAtSymbolOrWithEmptyEmailControllerThrowsValidationException() {
        User user = User.builder()
                .email("coldhobbit31ya.ru")
                .name("Vlad")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();

        BindingResult br = new BeanPropertyBindingResult(user, "user");

        br.addError(new FieldError("user", "email", "Ошибка валидации поля email"));

        assertThrows(ValidationException.class, () -> controller.createUser(user, br));

        user.setEmail("");

        br.addError(new FieldError("user", "email", "Ошибка валидации поля email"));

        assertThrows(ValidationException.class, () -> controller.createUser(user, br));

    }

    @Test
    public void withSpaceInLoginControllerThrowsValidationException() {

        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad Shutov")
                .birthday(LocalDate.parse("1990-12-31"))
                .build();
        BindingResult br = new BeanPropertyBindingResult(user, "user");

        br.addError(new FieldError("user", "login", "Ошибка валидации поля login"));

        assertThrows(ValidationException.class, () -> controller.createUser(user, br));

    }

    @Test
    public void withBirthdayIsAfterCurrentDayControllerThrowsValidationException() {
        User user = User.builder()
                .email("coldhobbit31@ya.ru")
                .login("Vlad_Shutov")
                .birthday(LocalDate.parse("2026-12-31"))
                .build();
        BindingResult br = new BeanPropertyBindingResult(user, "user");

        br.addError(new FieldError("user", "birthday", "Ошибка валидации поля birthday"));

        assertThrows(ValidationException.class, () -> controller.createUser(user, br));
    }
}
