package com.example.Travel.Booking.System.repository;

import com.example.Travel.Booking.System.entities.City;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.entities.TourInfo;
import com.example.Travel.Booking.System.enums.BusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {

    @Query("SELECT t FROM Tour t WHERE (t.sourceCity = :sourceCity AND t.destinationCity = :destinationCity AND t.busType = :busType)")
    Tour getBySourceCityAndDestinationCityAndBusType(
            @Param("sourceCity") City sourceCity,
            @Param("destinationCity") City destinationCity,
            @Param("busType") BusType busType
    );
    @Query("SELECT COUNT(t) > 0 FROM Tour t WHERE (t.sourceCity = :sourceCity AND t.destinationCity = :destinationCity AND t.busType = :busType)")
    boolean existsBySourceCityAndDestinationCityAndBusType(
            @Param("sourceCity") City sourceCity,
            @Param("destinationCity") City destinationCity,
            @Param("busType") BusType busType
    );

    //Tour findBySourceCityAndDestinationCityAndArrangeDateAndBusType(City sourceCity, City destinationCity, BusType busType);

}

