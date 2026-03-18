package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    void findByBookerIdAndEndBeforeOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Наташа");
        user1.setEmail("Romanova@Widow.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Клинт");
        user2.setEmail("Barton@Hawkeye.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Камень разума");
        item.setDescription("Один из камней бесконечности");
        item.setAvailable(true);
        item.setOwner(owner);
        Item sacrifice = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2019, 4, 29, 1, 53, 52));
        booking.setEnd(LocalDateTime.of(2019, 4, 29, 1, 53, 59));
        booking.setItem(sacrifice);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        Booking weight = bookingRepository.save(booking);

        List<Booking> bookings =
                bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(),LocalDateTime.now());

        assertTrue(bookings.getFirst().getEnd().isBefore(LocalDateTime.now()));
        assertEquals(bookings.getFirst().getId(), weight.getId());
    }
}