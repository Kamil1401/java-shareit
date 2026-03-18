package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void findByOwner_Id() {
        User user = new User();
        user.setName("Charles");
        user.setEmail("Xavier@school.com");
        User owner = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Cerebro");
        item1.setDescription("Устройство поиска людей и мутантов");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setName("Инвалидное кресло");
        item2.setDescription("Высокотехнологичное кресло");
        item2.setAvailable(true);
        item2.setOwner(owner);

        Item savedItem1 = itemRepository.save(item1);
        Item savedItem2 = itemRepository.save(item2);

        List<Item> items = itemRepository.findByOwner_Id(owner.getId());

        assertEquals(items.getFirst().getId(), savedItem1.getId());
        assertEquals(items.getLast().getId(), savedItem2.getId());
    }

    @Test
    void search() {
        User user = new User();
        user.setName("Stephen");
        user.setEmail("Strange@doctor.com");
        User owner = userRepository.save(user);

        Item item = new Item();
        item.setName("Камень времени");
        item.setDescription("Один из камней бесконечности");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        String text = "Камень";

        List<Item> items = itemRepository.search(text);

        assertEquals(items.getFirst().getId(), savedItem.getId());
    }
}