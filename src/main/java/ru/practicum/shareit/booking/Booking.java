package ru.practicum.shareit.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class Booking {

    @PositiveOrZero
    private Long id;

    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    private LocalDateTime end;

    @NotNull
    private Item item;

    @NotNull
    private User booker;

    private BookingStatus bookingStatus;
}