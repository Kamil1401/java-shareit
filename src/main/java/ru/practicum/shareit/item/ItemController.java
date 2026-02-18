package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/items")
@RestController
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                           @Valid @RequestBody ItemDto itemDto) {

        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                              @PathVariable @Positive Long itemId,
                              @RequestBody ItemDto dto) {

        return itemService.updateItem(userId, itemId, dto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable @Positive Long itemId) {
        return itemService.getAboutItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
