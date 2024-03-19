package com.example.Travel.Booking.System.validation;

import com.example.Travel.Booking.System.dto.TourDto;
import com.example.Travel.Booking.System.exception.DuplicateTourException;
import com.example.Travel.Booking.System.exception.InvalidDateException;
import com.example.Travel.Booking.System.exception.InvalidFormatException;
import com.example.Travel.Booking.System.exception.NullValueException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;

public class TourValidation {
    public static void validateTour(double cost, long travelDuration, LocalDate arrangeDate, LocalTime arrivalTime) {
        if (arrangeDate.equals("0")) {
            throw new NullValueException("Invalid input. Please provide value of arrange date for tour properties.");
        } else if (arrangeDate == null) {
            throw new NullValueException("Invalid input. Please provide value of arrange date for tour properties.");
        } else if (arrangeDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Please insert an arrange date greater than or equal to today's date!");
        } else if (arrivalTime == null) {
            throw new NullValueException("Invalid input. Please provide value of arrival time for tour properties.");
        } else if (cost <= 0) {
            throw new NullValueException("Invalid input. Please provide value of cost for tour properties.");
        } else if (travelDuration <= 0) {
            throw new NullValueException("Invalid input. Please provide value of travel duration for tour properties.");
        }
    }

    public static void validateTourDto(TourDto tourDto) {
        if (StringUtils.isBlank(tourDto.getSourceCityName())) {
            throw new NullValueException("Invalid input for source city Please provide values for all tour properties.");
        } else if (StringUtils.isBlank(tourDto.getDestinationCityName())) {
            throw new NullValueException("Invalid input for destination city Please provide values for all tour properties.");
        } else if (tourDto.getBusType().equals(" ") || tourDto.getBusType() == null) {
            throw new NullValueException("Invalid input for bus type Please provide values for all tour properties.");
        }
    }

    public static boolean isValidTourNames(String source, String destination) {
        int minLength = 2;
        int maxLength = 50;
        if (StringUtils.isNumeric(source) || StringUtils.isNumeric(destination) || source.toLowerCase().equals(destination.toLowerCase())) {
            return false;
        }
        return ((source.length() >= minLength && source.length() <= maxLength) && (destination.length() >= minLength && destination.length() <= maxLength));
    }

    public static void validCityName(String cityName) {
        if (StringUtils.isNumeric(cityName) || StringUtils.isBlank(cityName) || cityName.length() > 30) {
            throw new InvalidFormatException("Please enter city name in correct format");
        }
    }

    public static LocalTime getDepartureTime(long duration, LocalTime arrivalTime) {
        return arrivalTime.plusMinutes(duration);
    }

    public static void checkGetDetail(String source, String destination, LocalDate travelDate) {
        if (source.equals(destination)){
            throw new DuplicateTourException("Source and destination cities must be different....");
        }else if(StringUtils.isNumeric(source) || StringUtils.isBlank(source) || source.length()>30) {
            throw new NullValueException("Please fill correct value for source city");
        } else if (StringUtils.isNumeric(destination) || StringUtils.isBlank(destination)|| destination.length()>30) {
            throw new NullValueException("Please fill correct value for destination city");
        } else if (travelDate == null) {
            throw new NullValueException("Please fill value for travel date");
        } else if (travelDate.isBefore(LocalDate.now())) {
            throw new InvalidFormatException("Travel date must be greater than equal to today's date");
        }
    }
}
