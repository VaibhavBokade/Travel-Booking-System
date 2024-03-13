package com.example.Travel.Booking.System.service;

import com.example.Travel.Booking.System.dto.LoginDto;
import com.example.Travel.Booking.System.dto.TourBookingRequest;
import com.example.Travel.Booking.System.entities.User;

public interface UserService {
    User registerUser(User user);

    User loginUser(LoginDto loginDto);

   // User bookTour(TourBookingRequest tourBookingRequest);

    void cancelBooking(String userEmail, int tourId);
}
