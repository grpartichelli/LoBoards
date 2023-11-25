package com.marcoantonioaav.lobogames.exceptions;

public class FailedToWriteFileException extends RuntimeException {
    public FailedToWriteFileException() {
        super("Failed to write file");
    }
}