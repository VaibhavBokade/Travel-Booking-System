package com.example.Travel.Booking.System.repository;

import com.example.Travel.Booking.System.entities.City;
import com.example.Travel.Booking.System.entities.TourInfo;
import com.example.Travel.Booking.System.enums.BusType;
import com.example.Travel.Booking.System.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TourInfoRepository extends JpaRepository<TourInfo, Integer> {

    @Query("SELECT ti.travelDurationMinutes FROM TourInfo ti WHERE ti.sourceCity.cityName = :sourceCityName AND ti.destinationCity.cityName = :destinationCityName AND ti.busType = :busType")
    Long getTravelDurationMinutes(@Param("sourceCityName") String sourceCityName, @Param("destinationCityName") String destinationCityName, @Param("busType") BusType busType);

    @Query("SELECT COUNT(t) > 0 FROM TourInfo t WHERE (t.sourceCity = :sourceCity AND t.destinationCity = :destinationCity AND t.busType = :busType) OR (t.sourceCity = :destinationCity AND t.destinationCity = :sourceCity AND t.busType = :busType)")
    boolean existsBySourceCityAndDestinationCityAndBusTypeOrReverse(
            @Param("sourceCity") City sourceCity,
            @Param("destinationCity") City destinationCity,
            @Param("busType") BusType busType
    );

    @Query("SELECT t FROM TourInfo t WHERE (t.sourceCity = :sourceCity AND t.destinationCity = :destinationCity AND t.busType = :busType) OR (t.sourceCity = :destinationCity AND t.destinationCity = :sourceCity AND t.busType = :busType)")
    TourInfo getBySourceCityAndDestinationCityAndBusTypeOrReverse(
            @Param("sourceCity") City sourceCity,
            @Param("destinationCity") City destinationCity,
            @Param("busType") BusType busType
    );

    @Query("SELECT t FROM TourInfo t WHERE t.status = :status")
    List<TourInfo> getByStatus(@Param("status") Status status);

   @Query("SELECT t.sourceCity FROM TourInfo t WHERE t.id = :tourInfoId")
    City getSourceCityByTourInfoId(@Param("tourInfoId") int tourInfoId);

    @Query("SELECT t.destinationCity FROM TourInfo t WHERE t.id = :tourInfoId")
    City getDestinationCityByTourInfoId(@Param("tourInfoId") int tourInfoId);

    @Query("SELECT t.busType FROM TourInfo  t WHERE t.id= :tourInfoId")
    BusType getBusTypeByTourInfoId(@Param("tourInfoId")int tourInfoId);


}

