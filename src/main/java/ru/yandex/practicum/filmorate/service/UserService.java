package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage){
        this.userStorage = userStorage;
    }

    private List<User> findFriend(Long id){
        return userStorage.getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .toList();
    }

    public void addToFriendList(Long userId, Long userAddedToFriend) {
        findFriend(userId).getFirst().getFriends().add(userAddedToFriend);
        findFriend(userAddedToFriend).getFirst().getFriends().add(userId);
    }

    public void deleteFromFriendList(Long userId, Long userAddedToFriend) {
        findFriend(userId).getFirst().getFriends().remove(userAddedToFriend);
        findFriend(userAddedToFriend).getFirst().getFriends().remove(userId);
    }

    public List<User> getUserFriendList(Long id){
        return findFriend(id).getFirst().getFriends().stream()
                .map(userId -> findFriend(userId).getFirst())
                .toList();
    }


}
