package com.mangareader.service.error;

import com.mangareader.exception.BadRequestException;

public class UsernameAlreadyUsedException extends BadRequestException {

    public UsernameAlreadyUsedException() {
        super("Username has been used.");
    }
}
