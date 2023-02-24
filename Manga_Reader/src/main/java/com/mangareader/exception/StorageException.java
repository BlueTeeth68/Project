package com.mangareader.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }
}
