package com.example.Travel.Booking.System.dto;

import com.example.Travel.Booking.System.enums.BusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDto {

    private String sourceCityName;
    private String destinationCityName;
    @Enumerated(EnumType.STRING)
    private BusType busType;


//    @JsonFormat(pattern = "HH:mm")
//    private LocalTime arrivalTime;
//    private double cost;
//    private long travelDuration;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate arrangeDate;
}

