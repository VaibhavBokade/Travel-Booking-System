package com.example.Travel.Booking.System.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTourDetailRequestDto {
    private String source;
    private String destination;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate travelDate;
}
