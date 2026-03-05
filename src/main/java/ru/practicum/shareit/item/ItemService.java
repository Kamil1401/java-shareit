package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long ownerId, ItemDto dto);

    CommentDto addComment(Long userId, Long itemId, CommentCreateDto dto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto dto);

    Item getItemById(Long id);

    List<Item> findByOwnerId(Long userId);

    ItemDto getAboutItem(Long id);

    List<Item> getAllItems();

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);

    void deleteItem(Long itemId);
}