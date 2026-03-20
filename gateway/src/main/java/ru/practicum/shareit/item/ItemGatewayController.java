package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemGatewayController {
    private final ItemClient itemClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {

        log.info("Gateway: add item userId={}, item={}", userId, itemDto);

        return itemClient.addItem(userId, itemDto);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentCreateDto dto) {

        log.info("Gateway: add comment itemId={}, userId={}, dto={}", itemId, userId, dto);

        return itemClient.addComment(userId, itemId, dto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody ItemDto dto) {

        log.info("Gateway: update item itemId={}, userId={}", itemId, userId);

        return itemClient.updateItem(userId, itemId, dto);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable @Positive Long itemId) {

        log.info("Gateway: get item itemId={}", itemId);

        return itemClient.getItem(itemId);
    }


    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(USER_ID_HEADER) Long userId) {

        log.info("Gateway: get user items userId={}", userId);

        return itemClient.getUserItems(userId);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text) {

        log.info("Gateway: search items text={}", text);

        return itemClient.searchItems(text);
    }
}