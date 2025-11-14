package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public interface UserStorage {

    @Autowired
    public Map<Long, User> users = new HashMap<>();

    public List<User> getUsers();

    public User createUser(User user, BindingResult br);

    public User updateUser(User user, BindingResult br);
}
