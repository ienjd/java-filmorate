package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private InMemoryUserStorage inMemoryUserStorage;

    private UserService userService;

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return inMemoryUserStorage.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriendList(@PathVariable Long id){
        return userService.getUserFriendList(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult br) {
        return inMemoryUserStorage.createUser(user, br);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult br) {
        return inMemoryUserStorage.updateUser(user, br);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/friends/{friendId}")
    public void addToFriendList(@PathVariable Long id,
                                @PathVariable Long friendId){
        userService.addToFriendList(id, friendId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriendFromList(@PathVariable Long id,
                                     @PathVariable Long friendId){
        userService.deleteFromFriendList(id, friendId);
    }


}
