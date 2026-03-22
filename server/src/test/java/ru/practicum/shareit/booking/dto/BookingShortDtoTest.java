package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingShortDtoTest {

    @Test
    void allArgsConstructor() {
        BookingShortDto dto = new BookingShortDto(1L, 100L);

        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getBookerId());
    }

    @Test
    void builder() {
        BookingShortDto dto = BookingShortDto.builder()
                .id(2L)
                .bookerId(200L)
                .build();

        assertEquals(2L, dto.getId());
        assertEquals(200L, dto.getBookerId());
    }
}