package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserGatewayController {
    private final UserClient userClient;


    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto dto) {

        log.info("Gateway: create user {}", dto);

        return userClient.createUser(dto);
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive Long userId,
                                             @RequestBody UserDto dto) {

        log.info("Gateway: update user userId={}, dto={}", userId, dto);

        return userClient.updateUser(userId, dto);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @Positive Long userId) {

        log.info("Gateway: get user userId={}", userId);

        return userClient.getUser(userId);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive Long userId) {

        log.info("Gateway: delete user userId={}", userId);

        return userClient.deleteUser(userId);
    }
}