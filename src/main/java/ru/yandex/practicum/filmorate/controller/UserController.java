package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    private InMemoryUserStorage inMemoryUserStorage;

    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    @GetMapping
    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult br) {
        if (br.hasErrors()) {
            throw new ValidationException("Ошибка валидации поля " + br.getFieldError().getField());
        } else {
            user.setId(createUserId());
            user.setName(user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName());
            users.put(user.getId(), user);
            log.info("Создан пользователь " + user.getName());
            return user;
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult br) {
        User oldUser = users.get(user.getId());
        if (br.hasErrors()) {
            throw new ValidationException("Ошибка валидации поля " + br.getFieldError().getField());
        } else {
            users.put(oldUser.getId(), user);
            log.info("Обновлен пользователь " + user.getName());
            return user;
        }
    }

}
