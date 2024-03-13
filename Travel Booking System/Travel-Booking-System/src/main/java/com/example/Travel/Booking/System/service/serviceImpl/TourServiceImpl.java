package com.example.Travel.Booking.System.service.serviceImpl;

import com.example.Travel.Booking.System.repository.CityRepository;
import com.example.Travel.Booking.System.repository.TourInfoRepository;
import com.example.Travel.Booking.System.repository.TourRepository;
import com.example.Travel.Booking.System.service.TourService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourServiceImpl implements TourService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourInfoRepository tourInfoRepository;

    @Autowired
    private CityRepository cityRepository;


//    public Tour addTourInTour(TourInfo tourInfo, LocalTime arrivalTime, LocalDate tourDate) {
//        Tour tour = new Tour();
//        tour.setSourceCity(tourInfo.getSourceCity());
//        tour.setDestinationCity(tourInfo.getDestinationCity());
//        tour.setCost(tourInfo.getCost());
//        tour.setBusType(tourInfo.getBusType());
//        tour.setArrivalTime(arrivalTime);
//        tour.setDepartureTime(TourValidation.getDepartureTime(tourInfo.getTravelDurationMinutes(), arrivalTime));
//        tour.setArrangeDate(tourDate);
//        tourRepository.save(tour);
//        return tour;
//    }

//    @Override
//    public TourDto getTourDetails(String source, String destination) {
//        try {
//            logger.info("START :: CLASS :: TourServiceImpl :: METHOD :: getTourDetails");
//            Tour tour = tourRepository.findBySourceAndDestination(source, destination);
//            if (tour == null) {
//                throw new TourNotFoundException("This tour is not yet arrange....");
//            }
//            TourDto tourDto = new TourDto();
//            BeanUtils.copyProperties(tour, tourDto);
//            return tourDto;
//        } catch (TourNotFoundException e) {
//            logger.error("Tour Not Found Exception");
//            e.printStackTrace();
//            throw e;
//        } catch (Exception ex) {
//            logger.error("An error occurred during adding tour: " + ex.getMessage());
//            ex.printStackTrace();
//            throw new RuntimeException("Error fetching tour details.", ex);
//        } finally {
//            logger.info("END :: CLASS :: TourServiceImpl :: METHOD :: getTourDetails");
//        }
//    }

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
