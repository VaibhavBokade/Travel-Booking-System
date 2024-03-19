package com.example.Travel.Booking.System.service.serviceImpl;

import com.example.Travel.Booking.System.dto.TourDetailsInfoResponseDto;
import com.example.Travel.Booking.System.entities.City;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.exception.*;
import com.example.Travel.Booking.System.repository.CityRepository;
import com.example.Travel.Booking.System.repository.TourInfoRepository;
import com.example.Travel.Booking.System.repository.TourRepository;
import com.example.Travel.Booking.System.service.TourService;
import com.example.Travel.Booking.System.validation.TourValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TourServiceImpl implements TourService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourInfoRepository tourInfoRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Tour> getTourDetails(String source, String destination, LocalDate travelDate) {
        try {
            logger.info("START :: CLASS :: TourServiceImpl :: METHOD :: getTourDetails");
            TourValidation.checkGetDetail(source,destination,travelDate);
            TourValidation.isValidTourNames(source,destination);
            if (!cityRepository.existsByCityName(source)) {
                throw new CityNotFoundException("This source city is not present in our list....");
            } else if (!cityRepository.existsByCityName(destination)) {
                throw new CityNotFoundException("This destination city is not present in our list....");
            }
            City sourceCity = cityRepository.findByCityName(source);
            City destinationCity = cityRepository.findByCityName(destination);
            List<Tour> tours = tourRepository.getBySourceCityAndDestinationCityAndArrangeDate(sourceCity, destinationCity, travelDate);
            if (tours.isEmpty()) {
                throw new TourNotFoundException("This tour is not yet arrange....");
            }
            return tours;
        }catch (DuplicateTourException e) {
            logger.error("Duplicate Tour Exception");
            e.printStackTrace();
            throw e;
        } catch (NullValueException e) {
            logger.error("Null Value Exception");
            e.printStackTrace();
            throw e;
        }catch (InvalidFormatException e) {
            logger.error("Invalid Format Exception");
            e.printStackTrace();
            throw e;
        }catch (TourNotFoundException e) {
            logger.error("Tour Not Found Exception");
            e.printStackTrace();
            throw e;
        } catch (Exception ex) {
            logger.error("An error occurred during adding tour: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Error fetching tour details.", ex);
        } finally {
            logger.info("END :: CLASS :: TourServiceImpl :: METHOD :: getTourDetails");
        }
    }

    @Override
    public List<TourDetailsInfoResponseDto> getDetails(List<Tour>tours) {
        List<TourDetailsInfoResponseDto> responseDtoList = new ArrayList<>();
        for (Tour tour : tours) {
            TourDetailsInfoResponseDto tourDetailsInfoResponseDto = new TourDetailsInfoResponseDto();
            tourDetailsInfoResponseDto.setArrivalTime(tour.getArrivalTime());
            tourDetailsInfoResponseDto.setDepartureTime(tour.getDepartureTime());
            tourDetailsInfoResponseDto.setBusType(tour.getBusType());
            tourDetailsInfoResponseDto.setCost(tour.getCost());
            responseDtoList.add(tourDetailsInfoResponseDto);
        }
        return responseDtoList;
    }

    @Transactional
    public void updateAvailableSeats(int tourId, int totalSeats, int seatsBooked) {
        int availableSeats = totalSeats - seatsBooked;
        Tour tour = tourRepository.getById(tourId);
        if (tour != null) {
            tour.setNumberOfSeats(availableSeats);
            tourRepository.save(tour);
        }
    }

    @Transactional
    public void updateAvailableSeatsAfterCancelBooking(int tourId, int seatsCancel) {
        Tour tour = tourRepository.getById(tourId);
        int availableSeats = tour.getNumberOfSeats() + seatsCancel;
        if (tour != null) {
            tour.setNumberOfSeats(availableSeats);
            tourRepository.save(tour);
        }
    }

//    @Override
//    public Tour updateTour(int tourId, Tour updatedTour) {
//        try {
//            logger.info("START :: CLASS :: TourServiceImpl :: METHOD :: updateTour");
//            Tour existingTour = tourRepository.findById(tourId)
//                    .orElseThrow(() -> new TourNotFoundException("Tour with id " + tourId + " not found."));
//            if (updatedTour.getSource() != null) {
//                existingTour.setSource(updatedTour.getSource());
//            }
//            if (updatedTour.getDestination() != null) {
//                existingTour.setDestination(updatedTour.getDestination());
//            }
//            if (updatedTour.getPrice() != 0) {
//                existingTour.setPrice(updatedTour.getPrice());
//            } else if (updatedTour.getPrice() <= 0) {
//                throw new NullValueException("Rate should not be zero or negative !");
//            }
//            if (updatedTour.getArrangeDate() != null) {
//                existingTour.setArrangeDate(updatedTour.getArrangeDate());
//            }
//            return tourRepository.save(existingTour);
//        } catch (NullValueException e) {
//            logger.error("Null Value Exception");
//            e.printStackTrace();
//            throw e;
//        } catch (TourNotFoundException e) {
//            logger.error("Tour Not Found Exception");
//            e.printStackTrace();
//            throw e;
//        } catch (Exception ex) {
//            logger.error("An error occurred during adding tour: " + ex.getMessage());
//            ex.printStackTrace();
//            throw new RuntimeException("Error updating tour details.", ex);
//        }
//    }
}
