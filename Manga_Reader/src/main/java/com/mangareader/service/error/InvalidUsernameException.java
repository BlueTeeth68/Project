package com.mangareader.service.error;

import com.mangareader.exception.BadRequestException;

public class InvalidUsernameException extends BadRequestException {

    public InvalidUsernameException(String message) {
        super(message);
    }
}
