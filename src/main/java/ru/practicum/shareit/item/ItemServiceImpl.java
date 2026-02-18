package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IsNotTheOwnerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemInMemoryDao;
    private final UserService userService;


    @Override
    public ItemDto addItem(Long ownerId, ItemDto dto) {
        User owner = userService.getUserById(ownerId);
        Item item = ItemMapper.toItem(dto);
        item.setOwner(owner);
        Item savedItem = itemInMemoryDao.save(item);

        return ItemMapper.toDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto dto) {
        Item item = getItemById(itemId);

        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new IsNotTheOwnerException("Пользователь не является владельцем");
        }
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        Item updatedItem = itemInMemoryDao.save(item);

        return ItemMapper.toDto(updatedItem);
    }

    @Override
    public Item getItemById(Long id) {
        return itemInMemoryDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Данная вещь не зарегистрирована в каталоге"));
    }

    @Override
    public ItemDto getAboutItem(Long itemId) {
        Item item = getItemById(itemId);

        return ItemMapper.toDto(item);
    }

    @Override
    public List<Item> getAllItems() {
        return itemInMemoryDao.findAll();
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        userService.getUserById(userId);

        return itemInMemoryDao.findByOwnerId(userId).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemInMemoryDao.search(text).stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}