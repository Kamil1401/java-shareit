package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    @Positive
    private Long id;

    @NotNull(message = "Дата начала обязательна")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания обязательна")
    private LocalDateTime end;
    private BookingStatus status;

    private ItemShortDto item;
    private UserShortDto booker;
}