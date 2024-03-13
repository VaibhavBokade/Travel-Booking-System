package com.example.Travel.Booking.System.controller;

import com.example.Travel.Booking.System.cookies.CookieUtil;
import com.example.Travel.Booking.System.dto.LoginDto;
import com.example.Travel.Booking.System.dto.UserDto;
import com.example.Travel.Booking.System.entities.User;
import com.example.Travel.Booking.System.exception.*;
import com.example.Travel.Booking.System.service.UserService;
import com.example.Travel.Booking.System.validation.UserValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/travelBooking")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
        try {
            UserValidation.validateUser(user);
            User newUser = userService.registerUser(user);
            CookieUtil.addCookieForUser(response, newUser);
            String firstName = CookieUtil.getValue(request, "firstName");
            System.out.println("Getting the name form Cookies " + firstName);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistException | PasswordAlreadyExistException | InvalidUserException |
                 InvalidMobileNumberException | InvalidPasswordException | EmailValidationException |
                 InvalidFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            UserValidation.loginValidation(loginDto);
            User user = userService.loginUser(loginDto);
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            if (userDto != null) {
                CookieUtil.addCookieForUser(response, user);
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username / Password");
            }
        } catch (NullValueException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not exist with this credentials!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        try {
            CookieUtil.clearCookies(response);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during logout.");
        }
    }

//    @PostMapping("/bookTour")
//    public ResponseEntity<?> bookTour(@RequestBody TourBookingRequest tourBookingRequest, HttpServletResponse response, HttpServletRequest request) {
//        try {
//            User user = userService.bookTour(tourBookingRequest);
//            Notification.sendBookingEmail(user.getFirstName(), user.getLastName(), user.getEmail(), tourBookingRequest.getSource(), tourBookingRequest.getDestination(), tourBookingRequest.getPrice(), tourBookingRequest.getArrangeDate());
//            return ResponseEntity.ok("Tour booked successfully");
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.badRequest().body("User with this credential is not exist !");
//        } catch (TourNotExistException e) {
//            return ResponseEntity.badRequest().body("This tour is not present....!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during tour booking.");
//        }
//    }

    @DeleteMapping("/cancelBooking")
    public ResponseEntity<?> cancelBooking(HttpServletRequest request,
                                           @RequestHeader(value = "tourId") int tourId) {
        try {
            String email = CookieUtil.getValue(request, "email");
            userService.cancelBooking(email, tourId);
            return ResponseEntity.ok("Booking canceled successfully");
        } catch (BookingNotFoundException | UserNotFoundException | TourNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error canceling booking.");
        }
    }
}
