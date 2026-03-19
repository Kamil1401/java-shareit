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
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Very good item");
        item2.setAvailable(true);
        item2.setOwner(owner);
        Item savedItem2 = itemRepository.save(item2);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 3, 15, 18, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 15, 18, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 3, 18, 18, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 18, 18, 0));
        booking2.setItem(savedItem2);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findCurrentBookings(booker.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getLast().getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getLast().getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getId(), savedBooking2.getId());
        assertEquals(bookings.getLast().getId(), savedBooking1.getId());
    }

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

        Booking secondBooking = new Booking();
        secondBooking.setStart(LocalDateTime.of(2020, 4, 29, 1, 53, 52));
        secondBooking.setEnd(LocalDateTime.of(2020, 4, 29, 1, 53, 59));
        secondBooking.setItem(sacrifice);
        secondBooking.setBooker(booker);
        secondBooking.setStatus(BookingStatus.APPROVED);
        Booking weight2 = bookingRepository.save(secondBooking);

        List<Booking> bookings =
                bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(),LocalDateTime.now());

        assertTrue(bookings.getFirst().getEnd().isBefore(LocalDateTime.now()));
        assertEquals(bookings.getFirst().getId(), weight2.getId());
        assertEquals(bookings.getLast().getId(), weight.getId());
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartAsc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2030, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2030, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2030, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2030, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStartAfterOrderByStartAsc(booker.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getStart().isAfter(LocalDateTime.now()));
        assertEquals(bookings.getFirst().getId(), savedBooking1.getId());
        assertEquals(bookings.getLast().getId(), savedBooking2.getId());
        assertTrue(bookings.getFirst().getStart().isBefore(bookings.getLast().getStart()));
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.WAITING);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStatusOrderByStartDesc(booker.getId(), BookingStatus.WAITING);

        assertEquals(2, bookings.size());
        assertEquals(BookingStatus.WAITING, bookings.getFirst().getStatus());
        assertEquals(BookingStatus.WAITING, bookings.getLast().getStatus());
        assertEquals(savedBooking2.getId(), bookings.getFirst().getId());
        assertEquals(savedBooking1.getId(), bookings.getLast().getId());
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
    }
}