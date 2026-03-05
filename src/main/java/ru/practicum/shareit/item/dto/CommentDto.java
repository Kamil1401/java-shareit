package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter @Setter
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}