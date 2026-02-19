package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long ownerId, ItemDto dto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto dto);

    Item getItemById(Long id);

    ItemDto getAboutItem(Long id);

    List<Item> getAllItems();

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);
}