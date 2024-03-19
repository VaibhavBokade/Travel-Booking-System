package com.example.Travel.Booking.System.dto;

import com.example.Travel.Booking.System.enums.BusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBookingsResponse {
    private String sourceCity;
    private String destinationCity;
    private Double cost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrangeDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    private BusType busType;
    private int bookedSeats;
}
