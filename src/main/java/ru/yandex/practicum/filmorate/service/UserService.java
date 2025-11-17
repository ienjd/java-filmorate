package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    protected Optional<User> findFriend(Long id) {
        return Optional.ofNullable(userStorage.getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findAny().orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    public void addToFriendList(Long userId, Long userAddedToFriend) {
        findFriend(userId).get().getFriends().add(userAddedToFriend);
        findFriend(userAddedToFriend).get().getFriends().add(userId);
    }

    public void deleteFromFriendList(Long userId, Long userAddedToFriend) {
        findFriend(userId).get().getFriends().remove(userAddedToFriend);
        findFriend(userAddedToFriend).get().getFriends().remove(userId);
    }

    public List<User> getUserFriendList(Long id) {
        List<User> userFriendList = findFriend(id).get().getFriends().stream()
                .map(userId -> findFriend(userId).get())
                .collect(Collectors.toList());

        return userFriendList;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = getUserFriendList(id).stream()
                .filter(userId -> getUserFriendList(id).contains(userId) &&
                        getUserFriendList(otherId).contains(userId))
                .collect(Collectors.toList());

        return commonFriends;
    }
}
