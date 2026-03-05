package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;


    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto dto) {
        return userService.createUser(dto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable @Positive Long userId,
                              @RequestBody UserDto dto) {

        return userService.updateUser(userId, dto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable @Positive Long userId) {
        return userService.getAboutUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive Long userId) {
        userService.deleteUser(userId);
    }
}
