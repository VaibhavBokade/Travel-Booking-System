package com.example.Travel.Booking.System.dto;

import com.example.Travel.Booking.System.enums.BusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailsInfoResponseDto {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    private BusType busType;
    private Double cost;

}
