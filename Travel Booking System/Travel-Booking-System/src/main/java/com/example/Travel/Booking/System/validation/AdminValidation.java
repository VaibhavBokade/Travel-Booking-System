package com.example.Travel.Booking.System.validation;

import com.example.Travel.Booking.System.dto.AdminDto;
import com.example.Travel.Booking.System.exception.NullValueException;
import org.apache.commons.lang3.StringUtils;

public class AdminValidation {
    public static void validateAdminFields(AdminDto adminDto) {
        if (adminDto.getUserName() == null || adminDto.getUserName().isEmpty()) {
            throw new NullValueException("Username is required....");
        }
    }

    public static void loginValidation(AdminDto adminDto) {
        if (StringUtils.isBlank(adminDto.getUserName()) || StringUtils.isBlank(adminDto.getPassword())) {
            throw new NullValueException("Invalid input. Please provide values for all login credentials.");
        }
    }
}
