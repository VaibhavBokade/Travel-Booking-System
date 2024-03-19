package com.example.Travel.Booking.System.dto;

import com.example.Travel.Booking.System.enums.BusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {

    private String arrivalTime;
    private String departureTime;
    private BusType busType;
    private Double cost;
    private int numberOfSeats;
}
