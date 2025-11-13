package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    Map <Long, User> users;

    public InMemoryUserStorage(Map<Long, User> users) {
        this.users = users;
    }

    private Long createUserId() {
        long lostId = users.size();
        return ++lostId;
    }

    public List<User> getUsers() {
        return users.values().stream().toList();
    }



}
