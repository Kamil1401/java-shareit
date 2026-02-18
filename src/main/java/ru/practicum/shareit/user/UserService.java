package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto dto);

    UserDto updateUser(Long userId, UserDto dto);

    User getUserById(Long id);

    UserDto getUserDto(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);
}
