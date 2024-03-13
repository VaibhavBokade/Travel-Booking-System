package com.example.Travel.Booking.System.controller;

import com.example.Travel.Booking.System.dto.TourDto;
import com.example.Travel.Booking.System.entities.TourInfo;
import com.example.Travel.Booking.System.exception.DuplicateTourException;
import com.example.Travel.Booking.System.exception.InvalidDateException;
import com.example.Travel.Booking.System.exception.NullValueException;
import com.example.Travel.Booking.System.service.TourService;
import com.example.Travel.Booking.System.validation.TourValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/travelBooking")
public class TourController {



//    @GetMapping("/getTourDetails")
//    public ResponseEntity<?> getTourDetails(@RequestParam String source, @RequestParam String destination) {
//        try {
//            TourDto tourDetails = tourService.getTourDetails(source, destination);
//            return ResponseEntity.ok(tourDetails);
//        } catch (TourNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
//        }
//    }

//    @PutMapping("/updateTour")
//    public ResponseEntity<?> updateTour(@RequestHeader(value = "tourId") int tourId, @RequestBody Tour updatedTour) {
//        try {
//            Tour updatedTourDetails = tourService.updateTour(tourId, updatedTour);
//            return ResponseEntity.ok(updatedTourDetails);
//        } catch (TourNotFoundException | NullValueException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
//        }
//    }
}
