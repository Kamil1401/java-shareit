package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
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
class ItemRequestServiceImplTest {
    private final ItemRequestService requestService;
    private final UserService userService;
    private final ItemService itemService;
    private final EntityManager entityManager;


    @Test
    void getUserRequests() {
        UserDto firstUserDto = UserDto.builder()
                .name("Kurtis")
                .email("Stryker@mk.com")
                .build();

        UserDto secondDto = UserDto.builder()
                .name("Wade")
                .email("Wilson@hate-francis.com")
                .build();

        UserDto ownerDto = userService.createUser(firstUserDto);
        UserDto requestorDto = userService.createUser(secondDto);

        ItemRequestDto requestDto1 = ItemRequestDto.builder()
                .description("Нужен ствол")
                .created(LocalDateTime.of(2026, 3, 16, 10, 0))
                .build();

        ItemRequestDto requestDto2 = ItemRequestDto.builder()
                .description("Одного мало, нужен ещё ствол")
                .created(LocalDateTime.of(2026, 3, 16, 10, 5))
                .build();

        ItemRequestDto requestWithId1 = requestService.addRequest(requestorDto.getId(), requestDto1);
        ItemRequestDto requestWithId2 = requestService.addRequest(requestorDto.getId(), requestDto2);

        ItemDto firstItemDto = ItemDto.builder()
                .name("Desert Eagle Mark XIX")
                .description("Необходимо предъявить разрешение")
                .available(true)
                .requestId(requestWithId1.getId())
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .name("Beretta 92FS")
                .description("Необходимо предъявить разрешение")
                .available(true)
                .requestId(requestWithId2.getId())
                .build();

        ItemDto firstItemDtoWithId = itemService.addItem(ownerDto.getId(), firstItemDto);
        ItemDto secondItemDtoWithId = itemService.addItem(ownerDto.getId(), secondItemDto);

        List<ItemRequestDto> requests = requestService.getUserRequests(requestorDto.getId());

        ItemRequest request1 = entityManager.createQuery(
                "SELECT r FROM ItemRequest r WHERE r.id = :requestId", ItemRequest.class)
                .setParameter("requestId", requestWithId1.getId())
                .getSingleResult();

        ItemRequest request2 = entityManager.createQuery(
                        "SELECT r FROM ItemRequest r WHERE r.id = :requestId", ItemRequest.class)
                .setParameter("requestId", requestWithId2.getId())
                .getSingleResult();

        assertEquals(request2.getId(), requests.getFirst().getId());
        assertEquals(request1.getId(), requests.getLast().getId());
    }
}