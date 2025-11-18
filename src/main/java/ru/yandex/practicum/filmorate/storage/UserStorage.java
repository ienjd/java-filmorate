package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public interface UserStorage {

    @Autowired
    public Map<Long, User> users = new HashMap<>();

    public List<User> getUsers();

    public User createUser(User user);

    public User updateUser(User user);
}
