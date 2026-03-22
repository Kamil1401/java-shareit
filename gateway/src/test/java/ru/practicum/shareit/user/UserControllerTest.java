package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    private UserDto dto = UserDto.builder()
            .id(8L)
            .name("Arthur")
            .email("Curry@trident.com")
            .build();


    @Test
    void createUser_shouldBeOk() throws Exception {
        when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("Arthur"));
    }

    @Test
    void updateUser() throws Exception {
        when(userClient.updateUser(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(patch("/users/8")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("Arthur"));
    }

    @Test
    void getUser() throws Exception {
        when(userClient.getUser(anyLong()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(get("/users/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("Arthur"));
    }

    @Test
    void deleteUser() throws Exception {

        mvc.perform(delete("/users/8"))
                .andExpect(status().isOk());
    }
}