package com.mangareader.service.error;

import com.mangareader.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
