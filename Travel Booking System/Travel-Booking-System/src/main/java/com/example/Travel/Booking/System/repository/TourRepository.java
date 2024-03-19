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
import java.time.LocalTime;
import java.util.List;

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

    @Query("SELECT t FROM Tour t WHERE t.sourceCity = :sourceCity AND t.destinationCity = :destinationCity AND t.arrangeDate = :arrangeDate")
    List<Tour> getBySourceCityAndDestinationCityAndArrangeDate(@Param("sourceCity") City sourceCity,
                                                               @Param("destinationCity") City destinationCity,
                                                               @Param("arrangeDate") LocalDate arrangeDate
    );

    @Query("SELECT t FROM Tour t WHERE t.arrivalTime = :arrivalTime AND t.departureTime = :departureTime AND t.busType = :busType AND t.cost = :cost")
    Tour getByArrivalTimeAAndAndDepartureTimeAndBusTypeAndCost(@Param("arrivalTime") LocalTime arrivalTime,
                                                               @Param("departureTime") LocalTime departureTime,
                                                               @Param("busType") BusType busType,
                                                               @Param("cost") Double cost);


}

