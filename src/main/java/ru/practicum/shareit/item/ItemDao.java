package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {

    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findAll();

    List<Item> findByOwnerId(Long id);

    List<Item> search(String text);

    void delete(Long id);
}