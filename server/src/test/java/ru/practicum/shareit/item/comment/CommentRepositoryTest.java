package ru.practicum.shareit.item.comment;

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

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByItemIdOrderByCreatedAsc() {
        User user = new User();
        user.setName("Peter");
        user.setEmail("parker@dailybugle.com");
        User author = userRepository.save(user);

        User ownerUser = new User();
        ownerUser.setName("Tony");
        ownerUser.setEmail("stark@avengers.com");
        User owner = userRepository.save(ownerUser);

        Item item = new Item();
        item.setName("Web Shooter");
        item.setDescription("Снаряд для паутины");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Comment firstComment = new Comment();
        firstComment.setText("Первый комментарий");
        firstComment.setAuthor(author);
        firstComment.setItem(savedItem);
        firstComment.setCreated(LocalDateTime.of(2026, 3, 19, 10, 0));
        commentRepository.save(firstComment);

        Comment secondComment = new Comment();
        secondComment.setText("Второй комментарий");
        secondComment.setAuthor(author);
        secondComment.setItem(savedItem);
        secondComment.setCreated(LocalDateTime.of(2026, 3, 19, 12, 0));
        commentRepository.save(secondComment);

        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedAsc(savedItem.getId());

        assertEquals(2, comments.size());
        assertEquals("Первый комментарий", comments.get(0).getText());
        assertEquals("Второй комментарий", comments.get(1).getText());
    }
}