package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class UserDto {

    @Positive
    private Long id;

    @NotBlank(message = "Имя/логин обязательно для заполнения")
    private String name;

    @Email(message = "Некорректный адрес электронной почты")
    @NotBlank(message = "Почта обязательна для заполнения")
    private String email;
}