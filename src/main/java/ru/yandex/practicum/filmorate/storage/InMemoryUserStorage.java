package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Long createUserId() {
        long lostId = users.size();
        return ++lostId;
    }

    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findAny().orElseThrow(() -> new NotFoundException("Данный пользователь не найден")));
    }

    public User createUser(User user) {
        try {
            user.setId(createUserId());
            user.setName(user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName());
            user.setFriends(new HashSet<>());
            users.put(user.getId(), user);
            log.info("Создан пользователь " + user.getName());
            return user;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }


    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Данный пользователь не найден");
        }
        try {
            User oldUser = users.get(user.getId());
            users.put(oldUser.getId(), user);
            log.info("Обновлен пользователь " + user.getName());
            return user;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}


