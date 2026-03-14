package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class ItemRequestDto {
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime created;

    private List<ItemShortDto> items;
}