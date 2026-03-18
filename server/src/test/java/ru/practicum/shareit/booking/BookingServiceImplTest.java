package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final EntityManager entityManager;


    @Test
    void findBookingsByUserId() {
        UserDto firstUserDto = UserDto.builder()
                .name("J. Jonah")
                .email("Jameson@daily-bugle.com")
                .build();

        UserDto secondUserDto = UserDto.builder()
                .name("Collector")
                .email("Tivan@cygnus-x-1.com")
                .build();

        UserDto ownerUserDto = userService.createUser(firstUserDto);
        UserDto bookerUserDto = userService.createUser(secondUserDto);

        ItemDto firstItemDto = ItemDto.builder()
                .name("Костюм человека-паука")
                .description("Старый костюм размера M")
                .available(true)
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .name("Фото человека-паука")
                .description("Лучшие фотографии, сделанные Питером Паркером")
                .available(true)
                .build();

        ItemDto firstSavedItem = itemService.addItem(ownerUserDto.getId(), firstItemDto);
        ItemDto secondSavedItem = itemService.addItem(ownerUserDto.getId(), secondItemDto);

        BookingCreateDto bookingCreateDto1 = BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 2, 28, 15, 0))
                .end(LocalDateTime.of(2026, 3, 2, 15, 0))
                .itemId(firstSavedItem.getId())
                .build();

        BookingCreateDto bookingCreateDto2 = BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 3, 4, 15, 0))
                .end(LocalDateTime.of(2026, 3, 6, 15, 0))
                .itemId(secondSavedItem.getId())
                .build();

        BookingDto bookingDto1 = bookingService.create(bookerUserDto.getId(), bookingCreateDto1);
        BookingDto bookingDto2 = bookingService.create(bookerUserDto.getId(), bookingCreateDto2);

        List<BookingDto> pastBookings = bookingService.findBookingsByUserId(bookerUserDto.getId(), BookingState.PAST);

        Booking booking1 = entityManager.createQuery(
                "SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", bookingDto1.getId())
                .getSingleResult();

        Booking booking2 = entityManager.createQuery(
                        "SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", bookingDto2.getId())
                .getSingleResult();


        assertEquals(booking2.getId(), pastBookings.getFirst().getId());
        assertEquals(booking1.getId(), pastBookings.getLast().getId());
    }
}