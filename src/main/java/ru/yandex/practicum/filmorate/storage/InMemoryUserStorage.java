package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;

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

    public User getUser(Long id) {
        return users.values().stream().filter(user -> user.getId().equals(id)).toList().getFirst();
    }

    public User createUser(User user, BindingResult br) {

        if (br.hasErrors()) {
            throw new ValidationException("Ошибка валидации поля " + br.getFieldError().getField());
        } else {
            user.setId(createUserId());
            user.setName(user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName());
            user.setFriends(new HashSet<>());
            users.put(user.getId(), user);
            log.info("Создан пользователь " + user.getName());
            return user;
        }
    }

    public User updateUser(User user, BindingResult br) {
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
