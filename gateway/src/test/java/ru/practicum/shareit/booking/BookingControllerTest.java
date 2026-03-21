package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingClient bookingClient;

    private BookingDto dto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2024, 1, 1, 10, 0))
            .end(LocalDateTime.of(2024, 1, 2, 10, 0))
            .build();


    @Test
    void addBooking_shouldReturnOk() throws Exception {
        when(bookingClient.createBooking(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void confirmBooking_shouldReturnOk() throws Exception {
        when(bookingClient.confirmBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById_shouldReturnOk() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void getUserBookings_shouldReturnOk() throws Exception {
        when(bookingClient.getUserBookings(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(List.of(dto)));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookings_shouldReturnOk() throws Exception {
        when(bookingClient.getOwnerBookings(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(List.of(dto)));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}