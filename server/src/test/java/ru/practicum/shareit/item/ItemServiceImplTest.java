package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager entityManager;


    @Test
    void getUserItems() {
        UserDto userDto = UserDto.builder()
                .name("Кларк")
                .email("Kent@smallville.com")
                .build();
        UserDto savedUser = userService.createUser(userDto);

        ItemDto firstItemDto = ItemDto.builder()
                .name("Karcher CVH 3")
                .description("Пылесос для автомобиля")
                .available(true)
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .name("Палатка")
                .description("Туристическая палатка на 6 человек")
                .available(true)
                .build();

        itemService.addItem(savedUser.getId(), firstItemDto);
        itemService.addItem(savedUser.getId(), secondItemDto);

        List<ItemDto> items = itemService.getUserItems(savedUser.getId());


        Item firstItem = entityManager.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class)
                .setParameter("name", "Karcher CVH 3")
                .getSingleResult();

        Item secondItem = entityManager.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class)
                .setParameter("name", "Палатка")
                .getSingleResult();


        assertEquals(items.getFirst().getName(), firstItem.getName());
        assertEquals(items.getLast().getDescription(), secondItem.getDescription());
    }
}