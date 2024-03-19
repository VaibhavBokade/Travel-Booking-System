package com.example.Travel.Booking.System.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TourBookingRequest {
    private String userEmail;
    private String source;
    private String destination;
    private double price;
    private LocalDate arrangeDate;
    private int noOfSeats;
}

