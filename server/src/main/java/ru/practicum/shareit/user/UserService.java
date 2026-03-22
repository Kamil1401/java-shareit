package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto dto);

    UserDto updateUser(Long userId, UserDto dto);

    User getUserById(Long id);

    UserDto getAboutUser(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);
}
