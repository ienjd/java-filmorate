package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private InMemoryUserStorage inMemoryUserStorage;

    private UserService userService;

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public User getUser(@PathVariable @Positive Long id) {
        return inMemoryUserStorage.getUser(id).get();
    }

    @GetMapping("/{id}/friends")
    @Validated
    public List<User> getUserFriendList(@PathVariable @Positive Long id) {
        return userService.getUserFriendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Validated
    public List<User> getCommonFriends(@PathVariable @Positive Long id,
                                       @PathVariable @Positive Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @Validated
    public User createUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping
    @Validated
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/friends/{friendId}")
    @Validated
    public void addToFriendList(@PathVariable @Positive Long id,
                                @PathVariable @Positive Long friendId) {
        userService.addToFriendList(id, friendId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriendFromList(@PathVariable @Positive Long id,
                                     @PathVariable @Positive Long friendId) {
        userService.deleteFromFriendList(id, friendId);
    }
}
