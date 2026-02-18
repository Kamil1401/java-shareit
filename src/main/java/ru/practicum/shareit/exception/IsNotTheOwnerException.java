package ru.practicum.shareit.exception;

public class IsNotTheOwnerException extends RuntimeException {
    public IsNotTheOwnerException(String message) {
        super(message);
    }
}
