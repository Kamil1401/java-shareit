package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class BookingCreateDto {

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @Positive
    @NotNull
    private Long itemId;
}