package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDto {

    @Positive
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}