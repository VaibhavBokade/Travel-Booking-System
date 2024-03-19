package com.example.Travel.Booking.System.dto;

import com.example.Travel.Booking.System.enums.Status;
import lombok.Data;

@Data
public class TourRequestDto {
    private String sourceCity;
    private String destinationCity;
    private String busType;
    private Long travelDurationMinutes;
    private Double cost;
    private Status status;
}

