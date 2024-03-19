package com.example.Travel.Booking.System.service;

import com.example.Travel.Booking.System.dto.BookRequestDto;
import com.example.Travel.Booking.System.dto.GetBookingsResponse;
import com.example.Travel.Booking.System.dto.LoginDto;
import com.example.Travel.Booking.System.dto.TourDetailsInfoResponseDto;
import com.example.Travel.Booking.System.entities.User;
import com.example.Travel.Booking.System.entities.UserTour;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User loginUser(LoginDto loginDto);
    void cancelBooking(String userEmail, int tourId, int noOfCancelSeats);
    String bookTour(BookRequestDto bookRequestDto, String userEmail);
    List<GetBookingsResponse> showDetails(String userEmail);

}
