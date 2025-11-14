package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private List<User> findFriend(Long id) {
        return userStorage.getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .collect(Collectors.toList());
    }

    public void addToFriendList(Long userId, Long userAddedToFriend) {
        findFriend(userId).getFirst().getFriends().add(userAddedToFriend);
        findFriend(userAddedToFriend).getFirst().getFriends().add(userId);
    }

    public void deleteFromFriendList(Long userId, Long userAddedToFriend) {
        findFriend(userId).getFirst().getFriends().remove(userAddedToFriend);
        findFriend(userAddedToFriend).getFirst().getFriends().remove(userId);
    }

    public List<User> getUserFriendList(Long id) {
        List<User> userFriendList = findFriend(id).getFirst().getFriends().stream()
                .map(userId -> findFriend(userId).getFirst())
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
