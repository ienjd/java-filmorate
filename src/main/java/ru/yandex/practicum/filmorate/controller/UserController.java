package ru.yandex.practicum.filmorate.controller;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users;

    public UserController() {
        this.users = new HashMap<>();
    }

    private Long createUserId() {
        long lostId = users.size();
        return ++lostId;
    }

    private boolean emailIsCorrect(User user) {
        boolean emailIsCorrect = true;
        if (user.getEmail().isBlank() || user.getEmail().indexOf("@") == -1) {
            emailIsCorrect = false;
        }
        return emailIsCorrect;
    }

    private boolean loginIsCorrect(User user) {
        boolean loginIsCorrect = true;
        if (user.getLogin().isBlank() || user.getLogin().indexOf(" ") > -1) {
            loginIsCorrect = false;
        }
        return loginIsCorrect;
    }

    private boolean birthdayIsCorrect(User user) {
        boolean birthdayIsCorrect = true;
        if (user.getBirthday().isAfter(LocalDate.now())) {
            birthdayIsCorrect = false;
        }
        return birthdayIsCorrect;
    }

    private boolean nameIsBlank(User user) {
        return user.getName().isBlank();
    }

    @GetMapping
    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        if (
                (user.getEmail() == null) ||
                        (user.getLogin() == null) ||
                        (user.getBirthday() == null)) {
            throw new ValidationException("Обязательное поле должно быть заполнено");
        }

        if (user.getEmail() != null) {
            if (!emailIsCorrect(user)) {
                throw new ValidationException("Некорректный адрес электронной почты");
            }
        }

        if (user.getLogin() != null) {
            if (!loginIsCorrect(user)) {
                throw new ValidationException("Некорректный логин");
            }
        }

        if (user.getName() == null || nameIsBlank(user)) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() != null) {
            if (!birthdayIsCorrect(user)) {
                throw new ValidationException("Некорректная дата рождения");
            }
        }

        user.setId(createUserId());
        users.put(user.getId(), user);
        log.info("Создан пользователь " + user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        User oldUser = users.get(user.getId());

        if (user.getEmail() != null) {
            if (!emailIsCorrect(user)) {
                throw new ValidationException("Некорректный адрес электронной почты");
            }
            oldUser.setEmail(user.getEmail());
        }

        if (user.getLogin() != null) {
            if (!loginIsCorrect(user)) {
                throw new ValidationException("Некорректный логин");
            }
            oldUser.setLogin(user.getLogin());
        }

        if (user.getBirthday() != null) {
            if (!birthdayIsCorrect(user)) {
                throw new ValidationException("Некорректная дата рождения");
            }
            oldUser.setBirthday(user.getBirthday());
        }

        if (user.getName() != null) {
            if (nameIsBlank(user)) {
                oldUser.setName(oldUser.getName());
            }
            oldUser.setName(user.getName());
        }

        log.info("Обновлен пользователь " + oldUser.getName());
        return oldUser;
    }
}
