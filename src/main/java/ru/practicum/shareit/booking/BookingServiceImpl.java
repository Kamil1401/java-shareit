package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public BookingDto create(Long userId, BookingCreateDto dto) {

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Данная вещь не зарегистрирована в каталоге"));

        if (!item.getAvailable()) {
            throw new IllegalStateException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Владелец не может бронировать свою вещь");
        }
        if (dto.getStart() == null || dto.getEnd() == null) {
            throw new ValidationException("Дата начала и окончания обязательны");
        }
        if (!dto.getEnd().isAfter(dto.getStart())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }

        Booking booking = BookingMapper.fromCreateDto(dto);

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto confirmBooking(Long bookingId, Long userId, Boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не обнаружено"));

        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotOwnerException("Подтвердить или отклонить бронирование может только владелец");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Бронирование уже обработано");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemShortDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()))
                .booker(new UserShortDto(
                        booking.getBooker().getId(),
                        booking.getBooker().getName()))
                .build();
    }

    @Override
    public BookingDto getAboutBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не обнаружено"));

        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)
                && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new NotOwnerException("Запрос возможен только для владельца или арендатора");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findBookingsByUserId(Long userId, BookingState state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL -> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
            case CURRENT -> bookings = bookingRepository.findCurrentBookings(userId, now);
            case PAST -> bookings = bookingRepository.findPastBookings(userId, now);
            case FUTURE -> bookings = bookingRepository.findFutureBookings(userId, now);
            case WAITING -> bookings =
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> bookings =
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);

            default -> throw new IllegalArgumentException("Некорректный параметр состояния бронирования");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> findBookingsByItemOwnerId(Long userId, BookingState state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL -> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT -> bookings =
                    bookingRepository.findCurrentBookingsByItemOwnerId(userId, now);
            case PAST -> bookings =
                    bookingRepository.findPastBookingsByItemOwnerId(userId, now);
            case FUTURE -> bookings =
                    bookingRepository.findFutureBookingsByItemOwnerId(userId, now);
            case WAITING -> bookings =
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> bookings =
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);

            default -> throw new IllegalArgumentException("Некорректный параметр состояния бронирования");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
