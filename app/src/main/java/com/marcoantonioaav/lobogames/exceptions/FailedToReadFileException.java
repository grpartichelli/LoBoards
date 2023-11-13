package com.marcoantonioaav.lobogames.exceptions;

public class FailedToReadFileException extends RuntimeException {
    public FailedToReadFileException() {
        super("Failed to read file");
    }
}
