package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingGatewayController {
    private final BookingClient bookingClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid BookingDto dto) {

        log.info("Gateway: create booking userId={}, dto={}", userId, dto);

        return bookingClient.createBooking(userId, dto);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmBooking(@PathVariable Long bookingId,
                                                 @RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam Boolean approved) {

        log.info("Gateway: confirm booking bookingId={}, userId={}, approved={}",
                bookingId, userId, approved);

        return bookingClient.confirmBooking(bookingId, userId, approved);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PathVariable Long bookingId) {

        log.info("Gateway: get booking bookingId={}, userId={}", bookingId, userId);

        return bookingClient.getBooking(userId, bookingId);
    }


    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {

        log.info("Gateway: get bookings by user userId={}, state={}", userId, state);

        return bookingClient.getUserBookings(userId, state);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {

        log.info("Gateway: get bookings by owner ownerId={}, state={}", ownerId, state);

        return bookingClient.getOwnerBookings(ownerId, state);
    }
}