package me.sghong.manager.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String message;
    private final String location;

    public CustomException(String message, String location) {
        this.message = message;
        this.location = location;
    }
}
