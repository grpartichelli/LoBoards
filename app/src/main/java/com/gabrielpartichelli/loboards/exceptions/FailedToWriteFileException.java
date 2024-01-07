package com.gabrielpartichelli.loboards.exceptions;

public class FailedToWriteFileException extends RuntimeException {
    public FailedToWriteFileException() {
        super("Failed to write file");
    }
}