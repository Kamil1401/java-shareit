package ru.practicum.shareit.exception;

import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestController {

    @GetMapping("/not-found")
    public void throwNotFound() {
        throw new NotFoundException("Не обнаружено");
    }

    @GetMapping("/not-owner")
    public void throwNotOwner() {
        throw new NotOwnerException("Не является владельцем");
    }

    @GetMapping("/duplicate")
    public void throwDuplicate() {
        throw new DuplicateException("Уже существует");
    }

    @GetMapping("/validation")
    public void throwValidation() {
        throw new ValidationException("Ошибка валидации");
    }

    @GetMapping("/illegal")
    public void throwIllegal() {
        throw new IllegalStateException("Нельзя забронировать");
    }
}