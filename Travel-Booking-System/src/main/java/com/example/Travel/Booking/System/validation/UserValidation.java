package com.example.Travel.Booking.System.validation;

import com.example.Travel.Booking.System.dto.LoginDto;
import com.example.Travel.Booking.System.entities.User;
import com.example.Travel.Booking.System.exception.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {

    private static final int MAX_EMAIL_LENGTH = 50;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public static void validateUser(User user) throws InvalidUserException {
        if (StringUtils.isBlank(user.getFirstName()) || StringUtils.isBlank(user.getLastName()) || StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getMobileNumber())) {
            throw new InvalidUserException("Invalid input. Please provide values for all user properties.");
        } else if (!isValidName(user.getFirstName()) || !isValidName(user.getLastName())) {
            throw new InvalidFormatException("First name and last name should not contain numbers and should have a valid length.");
        }
    }

    private static boolean isValidName(String name) {
        if (StringUtils.isNumeric(name)) {
            return false;
        }
        int minLength = 2;
        int maxLength = 50;
        return name.length() >= minLength && name.length() <= maxLength;
    }

    public static void loginValidation(LoginDto loginDto) {
        if (StringUtils.isBlank(loginDto.getEmail()) || StringUtils.isBlank(loginDto.getPassword())) {
            throw new NullValueException("Invalid input. Please provide values for all login credentials.");
        }
    }

    public static String validateMobileNumber(String number) {
        if (number != null && number.length() == 10) {
            return number;
        } else {
            throw new InvalidMobileNumberException("This mobile number is not valid, please enter valid mobile number....");
        }
    }

    public static boolean isValidEmail(String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new EmailValidationException("Email must have length of 50 char....");
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new EmailValidationException("Enter email in valid format....");
        }
        return true;
    }

    public static String validatePassword(String password) {
        if (password != null && password.length() > 6) {
            return password;
        } else {
            throw new InvalidPasswordException("Password length must be greater than 15....");
        }
    }

    public static void validBookingsDetails(String source, String destination, LocalDate date) {
        if (StringUtils.isBlank(source) || StringUtils.isBlank(destination) || date == null) {
            throw new InvalidFormatException("Invalid input. Please provide required values.");
        } else if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Please insert arrange date greater than today's date !");

        }
    }
}
