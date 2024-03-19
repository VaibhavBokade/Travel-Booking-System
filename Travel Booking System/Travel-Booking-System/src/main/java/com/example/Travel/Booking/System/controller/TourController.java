package com.example.Travel.Booking.System.controller;

import com.example.Travel.Booking.System.dto.GetTourDetailRequestDto;
import com.example.Travel.Booking.System.dto.TourDetailsInfoResponseDto;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.exception.DuplicateTourException;
import com.example.Travel.Booking.System.exception.InvalidFormatException;
import com.example.Travel.Booking.System.exception.NullValueException;
import com.example.Travel.Booking.System.exception.TourNotFoundException;
import com.example.Travel.Booking.System.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelBooking")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping("/getTourDetails")
    public ResponseEntity<?> getTourDetails(@RequestBody GetTourDetailRequestDto getTourDetailRequestDto) {
        try {
            List<Tour> tourDetails = tourService.getTourDetails(getTourDetailRequestDto.getSource(), getTourDetailRequestDto.getDestination(), getTourDetailRequestDto.getTravelDate());
            List<TourDetailsInfoResponseDto> details = tourService.getDetails(tourDetails);
            return ResponseEntity.ok(details);
        } catch (DuplicateTourException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NullValueException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (TourNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

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
