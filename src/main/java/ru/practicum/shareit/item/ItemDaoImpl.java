package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ItemDaoImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();


    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(getNextId());
            items.put(item.getId(), item);
        }
        Item storedItem = items.get(item.getId());
        if (storedItem == null) {
            throw new NoSuchElementException("Пользователь с id = " + item.getId() + " не найден");
        }

        storedItem = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
        items.put(storedItem.getId(), storedItem);

        return storedItem;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .toList();
    }

    @Override
    public List<Item> search(String text) {
        String lowerText = text.toLowerCase();

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText)
                                        || item.getDescription().toLowerCase().contains(lowerText))
                .toList();
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }


    private long getNextId() {
        long currentMaxId = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
