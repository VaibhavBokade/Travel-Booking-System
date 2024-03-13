package com.example.Travel.Booking.System.service;

import com.example.Travel.Booking.System.dto.AdminDto;
import com.example.Travel.Booking.System.dto.TourDto;
import com.example.Travel.Booking.System.dto.TourRequestDto;
import com.example.Travel.Booking.System.entities.Admin;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.entities.TourInfo;

public interface AdminService {
    //boolean checkTourPresent(TourDto tourDto);
    boolean checkDetailsAvailable(String arrivalTime, double cost, String arrangeDate, long travelDuration);

    boolean checkTourPresent(TourDto tourDto);

    // boolean checkTourPresent(TourDto tourDto, String arrivalTime,double cost,String arrangeDate,long travelDuration);
    Tour addTourInTourInfo(TourDto tourDto, String arrivalTime, double cost, long travelDuration, String arrangeDate);

    String deleteCity(String cityName);

    void initializeAdmin();

    Admin loginAdmin(AdminDto adminDto);

    boolean isAdminCreationAllowed();

    TourInfo updateTourInfo(int tourInfoId, TourRequestDto updatedTour);


}
