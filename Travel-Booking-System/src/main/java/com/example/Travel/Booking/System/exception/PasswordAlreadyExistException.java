package com.example.Travel.Booking.System.exception;

public class PasswordAlreadyExistException extends RuntimeException {
    public PasswordAlreadyExistException(String message) {
        super(message);
    }
}
