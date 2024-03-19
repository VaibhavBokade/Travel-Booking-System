package com.example.Travel.Booking.System.exception;

public class TourNotExistException extends RuntimeException {
    public TourNotExistException(String message) {
        super(message);
    }
}
