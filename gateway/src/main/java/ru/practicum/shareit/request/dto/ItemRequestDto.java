package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ItemRequestDto {

    @Positive
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime created;

    private List<ItemShortDto> items;
}