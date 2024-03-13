package com.example.Travel.Booking.System.repository;

import com.example.Travel.Booking.System.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUserName(String userName);
}

