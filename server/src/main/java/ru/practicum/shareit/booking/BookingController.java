package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/bookings")
@RestController
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @RequestBody BookingCreateDto dto) {

        return bookingService.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmBooking(@PathVariable Long bookingId,
                                     @RequestHeader(USER_ID_HEADER) Long userId,
                                     @RequestParam Boolean approved) {

        return bookingService.confirmBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @PathVariable Long bookingId) {

        return bookingService.getAboutBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                            @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.findBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                             @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.findBookingsByItemOwnerId(ownerId, state);
    }

}
