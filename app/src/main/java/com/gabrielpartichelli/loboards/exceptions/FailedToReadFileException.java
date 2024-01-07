package com.gabrielpartichelli.loboards.exceptions;

public class FailedToReadFileException extends RuntimeException {
    public FailedToReadFileException() {
        super("Failed to read file");
    }
}
