package com.example.Travel.Booking.System;

import com.example.Travel.Booking.System.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class TravelBookingSystemApplication {
    @Autowired
    private AdminService adminService;


    public static void main(String[] args) {
        SpringApplication.run(TravelBookingSystemApplication.class, args);

    }

    @PostConstruct
    public void init() {
        adminService.isAdminCreationAllowed();
    }
}
