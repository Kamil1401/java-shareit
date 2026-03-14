package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {


    public static ItemRequestDto toDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public static ItemRequest toEntity(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();

        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setCreated(dto.getCreated());

        return request;
    }
}
