package com.example.Travel.Booking.System.service;

import com.example.Travel.Booking.System.dto.TourDetailsInfoResponseDto;
import com.example.Travel.Booking.System.entities.Tour;

import java.time.LocalDate;
import java.util.List;

public interface TourService {
    List<Tour> getTourDetails(String source, String destination, LocalDate travelDate);
    List<TourDetailsInfoResponseDto> getDetails(List<Tour>tours);
    void updateAvailableSeats(int tourId, int totalSeats, int seatsBooked);
    void updateAvailableSeatsAfterCancelBooking(int tourId, int seatsCancel);

}
