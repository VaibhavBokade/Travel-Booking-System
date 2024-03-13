package com.example.Travel.Booking.System.repository;

import com.example.Travel.Booking.System.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City,Integer> {
    City findByCityName(String name);
    boolean existsByCityName(String name);
    City getIdByCityName(String name);
}
