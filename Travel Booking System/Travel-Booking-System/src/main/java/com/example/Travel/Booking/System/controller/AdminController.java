package com.example.Travel.Booking.System.controller;

import com.example.Travel.Booking.System.cookies.CookieUtil;
import com.example.Travel.Booking.System.dto.AdminDto;
import com.example.Travel.Booking.System.dto.TourDto;
import com.example.Travel.Booking.System.dto.TourRequestDto;
import com.example.Travel.Booking.System.entities.Admin;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.entities.TourInfo;
import com.example.Travel.Booking.System.exception.*;
import com.example.Travel.Booking.System.service.AdminService;
import com.example.Travel.Booking.System.validation.AdminValidation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/travelBooking")
public class AdminController {
    @Autowired
    private AdminService adminService;


    @PostMapping("/addTour")
    public ResponseEntity<?> addTour(
            @RequestBody TourDto tourDto,
            @ApiParam(value = "arrivalTime") @RequestHeader(value = "arrivalTime", required = false) String arrivalTime,
            @ApiParam(value = "cost") @RequestHeader(value = "cost", required = false) Double cost,
            @ApiParam(value = "travelDuration") @RequestHeader(value = "travelDuration", required = false) Long travelDuration,
            @ApiParam(value = "arrangeDate") @RequestHeader(value = "arrangeDate", required = false) String arrangeDate) {
        try {
            Tour tour = adminService.addTourInTourInfo(tourDto, arrivalTime, cost, travelDuration, arrangeDate);
            return new ResponseEntity<>(tour,HttpStatus.CREATED);
        } catch (InvalidFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TourNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateTourException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidDateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NullValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/checkTour")
    public ResponseEntity<?> addTour(@RequestBody TourDto tourDto) {
        try {
            boolean checked = adminService.checkTourPresent(tourDto);
            if (!checked) {
                throw new TourNotFoundException("First register this tour...");
            } else {
                return new ResponseEntity<>("Tour is already present", HttpStatus.FOUND);
            }
        } catch (InvalidFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TourNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateTourException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidDateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NullValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PutMapping("/updateTourInfo")
    public ResponseEntity<?> updateTourInfo(@RequestHeader(value = "tourInfoId") int tourInfoId, @RequestBody TourRequestDto tourRequestDto) {
        try{
            TourInfo tourInfo = adminService.updateTourInfo(tourInfoId, tourRequestDto);
            return new ResponseEntity<>(tourInfo,HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (CityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (InvalidFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NullValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @DeleteMapping("/deleteCity")
    public ResponseEntity<?> DeleteCity(@RequestHeader(value = "cityName") String cityName) {
        try {
            String deletedCity = adminService.deleteCity(cityName);
            return new ResponseEntity<>(deletedCity, HttpStatus.OK);
        } catch (InvalidFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    @PostMapping(value = "/loginAdmin")
    public ResponseEntity<?> login(@RequestBody AdminDto adminDto, HttpServletResponse response) {
        try {
            Admin admin = adminService.loginAdmin(adminDto);
            AdminDto adminDto1 = new AdminDto();
            BeanUtils.copyProperties(admin, adminDto1);
            if (adminDto != null) {
                CookieUtil.addCookieForAdmin(response, adminDto.getUserName());
                return ResponseEntity.ok(adminDto);
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

    @PutMapping("/cancelTour")
    public ResponseEntity<?> cancelTour(@RequestHeader(value = "tourInfoId")int tourInfoId) {
        try{
            String cancelledMessage = adminService.cancelTour(tourInfoId);
            return new ResponseEntity<>(cancelledMessage,HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login.");
        }
    }
}
