package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestGatewayController.class)
class ItemRequestGatewayControllerTest {

    @MockBean
    private ItemRequestClient requestClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    private ItemRequestDto dto = ItemRequestDto.builder()
            .id(1L)
            .description("Ищу перфоратор")
            .created(LocalDateTime.of(2026, 1, 1, 10, 0))
            .build();


    @Test
    void addRequest_shouldReturnOk() throws Exception {
        when(requestClient.addRequest(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void getUserRequests_shouldReturnOk() throws Exception {
        when(requestClient.getUserRequests(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(dto)));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_shouldReturnOk() throws Exception {
        when(requestClient.getAllRequests(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(dto)));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequest_shouldReturnOk() throws Exception {
        when(requestClient.getRequestById(anyLong()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }
}