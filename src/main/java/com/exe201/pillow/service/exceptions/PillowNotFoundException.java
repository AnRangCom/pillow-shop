package com.exe201.pillow.service.exceptions;

public class PillowNotFoundException extends RuntimeException {

    public PillowNotFoundException(Long id) {
        super("Pillow not found with id: " + id);
    }
}
