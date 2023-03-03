package com.mangareader.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ToString
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }
}
