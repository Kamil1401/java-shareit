package ru.practicum.shareit.booking.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookingShortDto {
    private Long id;
    private Long bookerId;
}