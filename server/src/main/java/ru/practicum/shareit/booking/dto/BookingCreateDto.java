package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}