package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId
              AND b.start <= :time
              AND b.end >= :time
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentBookings(Long bookerId, LocalDateTime time);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
              AND b.end < ?2
            ORDER BY b.start DESC
            """)
    List<Booking> findPastBookings(Long bookerId, LocalDateTime time);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
              AND b.start > ?2
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureBookings(Long bookerId, LocalDateTime time);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.start <= :time
              AND b.end >= :time
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentBookingsByItemOwnerId(Long ownerId, LocalDateTime time);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.end < :time
            ORDER BY b.start DESC
            """)
    List<Booking> findPastBookingsByItemOwnerId(Long ownerId, LocalDateTime time);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.start > :time
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureBookingsByItemOwnerId(Long ownerId, LocalDateTime time);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
              AND b.start < :time
              AND b.status = 'APPROVED'
            ORDER BY b.start DESC
            """)
    List<Booking> findLast(Long itemId, LocalDateTime time);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
              AND b.start > :time
              AND b.status = 'APPROVED'
            ORDER BY b.start ASC
            """)
    List<Booking> findNext(Long itemId, LocalDateTime time);


    @Query("""
       SELECT b
       FROM Booking b
       WHERE b.item.id = :itemId
         AND b.booker.id = :userId
         AND b.status = 'APPROVED'
         AND b.end < :time
       """)
    List<Booking> findCompletedBooking(Long itemId, Long userId, LocalDateTime time);

    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.item.id = :itemId
              AND b.booker.id = :bookerId
              AND b.status = :status
              AND b.end < :time
            """)
    boolean existsCompletedBooking(
            Long itemId, Long bookerId, BookingStatus status, LocalDateTime time);
}