package com.mangareader.service.error;

public class UsernameAlreadyUsedException extends RuntimeException{

    public UsernameAlreadyUsedException() {
        super("Username has been used.");
    }
}
