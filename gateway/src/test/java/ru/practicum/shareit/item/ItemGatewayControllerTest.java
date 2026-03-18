package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemGatewayController.class)
class ItemGatewayControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Вещь")
            .description("Описание")
            .available(true)
            .build();

    private CommentCreateDto commentDto =
            new CommentCreateDto("Отличная вещь");


    @Test
    void addItem_shouldReturnOk() throws Exception {
        when(itemClient.addItem(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void addComment_shouldReturnOk() throws Exception {
        when(itemClient.addComment(anyLong(), anyLong(), any()))
                .thenReturn(ResponseEntity.ok(commentDto));

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_shouldReturnOk() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItem_shouldReturnOk() throws Exception {
        when(itemClient.getItem(anyLong()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void getUserItems_shouldReturnOk() throws Exception {
        when(itemClient.getUserItems(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(itemDto)));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItem_shouldReturnOk() throws Exception {
        when(itemClient.searchItems(anyString()))
                .thenReturn(ResponseEntity.ok(List.of(itemDto)));

        mvc.perform(get("/items/search")
                        .param("text", "item"))
                .andExpect(status().isOk());
    }


}