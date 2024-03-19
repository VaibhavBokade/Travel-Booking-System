package com.example.Travel.Booking.System.exception;

public class DuplicateTourException extends RuntimeException {
    public DuplicateTourException(String message) {
        super(message);
    }
}
