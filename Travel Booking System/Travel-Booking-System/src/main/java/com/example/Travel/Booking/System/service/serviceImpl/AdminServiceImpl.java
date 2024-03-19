package com.example.Travel.Booking.System.service.serviceImpl;

import com.example.Travel.Booking.System.dto.AdminDto;
import com.example.Travel.Booking.System.dto.TourDto;
import com.example.Travel.Booking.System.dto.TourRequestDto;
import com.example.Travel.Booking.System.entities.Admin;
import com.example.Travel.Booking.System.entities.City;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.entities.TourInfo;
import com.example.Travel.Booking.System.enums.BusType;
import com.example.Travel.Booking.System.exception.*;
import com.example.Travel.Booking.System.repository.AdminRepository;
import com.example.Travel.Booking.System.repository.CityRepository;
import com.example.Travel.Booking.System.repository.TourInfoRepository;
import com.example.Travel.Booking.System.repository.TourRepository;
import com.example.Travel.Booking.System.service.AdminService;
import com.example.Travel.Booking.System.validation.AdminValidation;
import com.example.Travel.Booking.System.validation.TourValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.Travel.Booking.System.enums.BusType.AC_SLEEPER_BUS;
import static com.example.Travel.Booking.System.enums.BusType.NON_AC_SLEEPER_BUS;
import static com.example.Travel.Booking.System.enums.Status.CANCEL;
import static com.example.Travel.Booking.System.enums.Status.COMPLETED;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourInfoRepository tourInfoRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EncryptionService encryptionService;


    private static Tour addTourInTour(City sourceCity, City destinationCity, BusType busType, LocalTime arrivalTime, double cost, long travelDuration, LocalDate arrangeDate) {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: addTourInTour");
        Tour tour = new Tour();
        tour.setSourceCity(sourceCity);
        tour.setDestinationCity(destinationCity);
        tour.setCost(cost);
        tour.setBusType(busType);
        tour.setArrivalTime(arrivalTime);
        tour.setDepartureTime(TourValidation.getDepartureTime(travelDuration, arrivalTime));
        tour.setArrangeDate(arrangeDate);
        logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: addTourInTour");
        return tour;
    }
    @Override
    public boolean checkDetailsAvailable(String arrivalTime, double cost, String arrangeDate, long travelDuration) {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: checkDetailsAvailable");
        boolean isPresent = true;
        if (arrangeDate.equals("0") || arrivalTime.equals("0") || travelDuration <= 0 || cost <= 0) {
            isPresent = false;
            return isPresent;
        }
        logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: checkDetailsAvailable");
        return isPresent;
    }

    @Override
    public boolean checkTourPresent(TourDto tourDto) {
        try {
            logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: checkTourPresent");
            TourValidation.validateTourDto(tourDto);
            boolean isTourPresent = true;
            City sourceCity = cityRepository.findByCityName(tourDto.getSourceCityName());
            City destinationCity = cityRepository.findByCityName(tourDto.getDestinationCityName());
            if (sourceCity.getCityName().equals(destinationCity.getCityName())) {
                throw new InvalidFormatException("Both source and destination can not be same");
            }
            BusType busType = tourDto.getBusType();
            if (!tourInfoRepository.existsBySourceCityAndDestinationCityAndBusTypeOrReverse(sourceCity, destinationCity, busType)) {
                if (!tourRepository.existsBySourceCityAndDestinationCityAndBusType(sourceCity, destinationCity, busType)) {
                    isTourPresent = false;
                }
            }
            return isTourPresent;
        } catch (NullValueException e) {
            logger.error("Invalid Format Exception");
            e.printStackTrace();
            throw e;
        } catch (InvalidFormatException e) {
            logger.error("Invalid Format Exception");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred during adding tour: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error during adding tour.", e);
        } finally {
            logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: checkTourPresent");
        }
    }

    @Override
    public Tour addTourInTourInfo(TourDto tourDto, String arrivalTime, double cost, long travelDuration, String arrangeDate) {
        try {
            logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: addTourInTourInfo");
            LocalTime arrivalTimeLocal = LocalTime.parse(arrivalTime);
            LocalDate arrangeDateLocal = LocalDate.parse(arrangeDate);
            TourValidation.validateTourDto(tourDto);
            TourValidation.validateTour(cost, travelDuration, arrangeDateLocal, arrivalTimeLocal);
            City sourceCity = cityRepository.findByCityName(tourDto.getSourceCityName());
            City destinationCity = cityRepository.findByCityName(tourDto.getDestinationCityName());
            if (!TourValidation.isValidTourNames(tourDto.getSourceCityName(), tourDto.getDestinationCityName())) {
                throw new InvalidFormatException("Please enter proper source and destination names.");
            } else if (!cityRepository.existsByCityName(tourDto.getSourceCityName()) || !cityRepository.existsByCityName(tourDto.getDestinationCityName())) {
                throw new CityNotFoundException("This city is not present in our list....");
            }
            if (tourInfoRepository.existsBySourceCityAndDestinationCityAndBusTypeOrReverse(sourceCity, destinationCity, tourDto.getBusType())) {
                if (tourRepository.existsBySourceCityAndDestinationCityAndBusType(sourceCity, destinationCity, tourDto.getBusType())) {
                    throw new DuplicateTourException("This tour is already present...");
                } else {
                    Tour tour = addTourInTour(sourceCity, destinationCity, tourDto.getBusType(), arrivalTimeLocal, cost, travelDuration, arrangeDateLocal);
                    tourRepository.save(tour);
                    return tour;
                }
            }
            TourInfo tourInfo = new TourInfo();
            tourInfo.setSourceCity(sourceCity);
            tourInfo.setDestinationCity(destinationCity);
            tourInfo.setBusType(tourDto.getBusType());
            tourInfo.setCost(cost);
            tourInfo.setTravelDurationMinutes(travelDuration);
            tourInfoRepository.save(tourInfo);
            Tour tour = addTourInTour(sourceCity, destinationCity, tourDto.getBusType(), arrivalTimeLocal, cost, travelDuration, arrangeDateLocal);
            tourRepository.save(tour);
            return tour;
        } catch (InvalidFormatException e) {
            logger.error("Invalid Format Exception");
            e.printStackTrace();
            throw e;
        } catch (DuplicateTourException e) {
            logger.error("Duplicate Tour Exception");
            e.printStackTrace();
            throw e;
        } catch (InvalidDateException e) {
            logger.error("Invalid Date Exception");
            e.printStackTrace();
            throw e;
        } catch (CityNotFoundException e) {
            logger.error("City Not Found Exception");
            e.printStackTrace();
            throw e;
        } catch (NullValueException e) {
            logger.error("Null Value Exception");
            e.printStackTrace();
            throw e;
        } catch (DateTimeParseException e) {
            logger.error("Invalid Date Exception");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred during adding tour: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error during adding tour.", e);
        } finally {
            logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: addTourInTourInfo");
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void createFixedScheduledBuses() {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: createFixedScheduledBuses");
        List<TourDto> predefinedTours = getPredefinedTours();
        for (TourDto tourDto : predefinedTours) {
            String sourceCityName = tourDto.getSourceCityName();
            String destinationCityName = tourDto.getDestinationCityName();
            BusType busType = tourDto.getBusType();
            City sourceCity = cityRepository.findByCityName(sourceCityName);
            City destCity = cityRepository.findByCityName(destinationCityName);
            TourInfo tourDetails = tourInfoRepository.getBySourceCityAndDestinationCityAndBusTypeOrReverse(sourceCity, destCity, busType);
            addTourInTour(sourceCity, destCity, busType, getArrivalTime(sourceCity.getCityName(), destCity.getCityName()), tourDetails.getCost(), tourDetails.getTravelDurationMinutes(), LocalDate.now());
            logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: createFixedScheduledBuses");
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeCompletedTours() {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: removeCompletedTours");
        List<TourInfo> completed = tourInfoRepository.getByStatus(COMPLETED);
        tourInfoRepository.deleteAll(completed);
        logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: removeCompletedTours");
    }

    private LocalTime getArrivalTime(String sourceCity, String destinationCity) {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: getArrivalTime");
        if ((sourceCity.equals("Pune") && destinationCity.equals("Yavatmal")) || (sourceCity.equals("Yavatmal") && destinationCity.equals("Pune"))) {
            return LocalTime.of(18, 0);
        } else if ((sourceCity.equals("Pune") && destinationCity.equals("Nagpur")) || (sourceCity.equals("Nagpur") && destinationCity.equals("Pune"))) {
            return LocalTime.of(18, 30);
        } else if (sourceCity.equals("Mumbai") && destinationCity.equals("Pune")) {
            return LocalTime.of(9, 0);
        }
        logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: getArrivalTime");
        throw new IllegalArgumentException("Invalid source and destination cities");
    }

    private List<TourDto> getPredefinedTours() {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: getPredefinedTours");
        List<TourDto> predefinedTours = new ArrayList<>();
        predefinedTours.add(new TourDto("Pune", "Yavatmal", NON_AC_SLEEPER_BUS));
        predefinedTours.add(new TourDto("Yavatmal", "Pune", NON_AC_SLEEPER_BUS));
        predefinedTours.add(new TourDto("Pune", "Nagpur", AC_SLEEPER_BUS));
        predefinedTours.add(new TourDto("Nagpur", "Pune", AC_SLEEPER_BUS));
        predefinedTours.add(new TourDto("Mumbai", "Pune", AC_SLEEPER_BUS));
//        for (TourDto tour:predefinedTours) {
//            String sourceCityName = tour.getSourceCityName();
//            String destinationCityName = tour.getDestinationCityName();
//            BusType busType = tour.getBusType();
//            predefinedTours.add(new TourDto(sourceCityName,destinationCityName,busType));
//        }
        logger.info("END :: CLASS :: AdminServiceImpl :: METHOD :: getPredefinedTours");
        return predefinedTours;
    }

    @Override
    public void initializeAdmin() {
        try {
            logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: initializeAdmin");
            if (adminRepository.count() == 0) {
                Admin admin = new Admin();
                admin.setUserName("admin");
                String password = encryptionService.encryptPassword("admin123");
                admin.setPassword(password);
                adminRepository.save(admin);
            } else {
                throw new UserAlreadyExistException("Admin credentials are already registered !!");
            }
        } catch (UserAlreadyExistException e) {
            logger.error("User Already Exist Exception");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting city: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("An error occurred while adding admin ", e);
        }finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: initializeAdmin");
        }
    }

    public boolean isAdminCreationAllowed() {
        logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: addTourInTourInfo");
        if (adminRepository.count() > 0) {
            return false;
        } else {
            initializeAdmin();
            return true;
        }

    }

    @Override
    public Admin loginAdmin(AdminDto adminDto) {
        try {
            logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: loginAdmin");
            AdminValidation.loginValidation(adminDto);
            Admin admin = adminRepository.findByUserName(adminDto.getUserName());
            if (admin != null && encryptionService.checkPassword(adminDto.getPassword(), admin.getPassword())) {
                return admin;
            } else {
                throw new UserNotFoundException("Invalid Username / Password");
            }
        } catch (UserNotFoundException e) {
            logger.error("Admin not found Exception");
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred during login: " + e.getMessage());
            throw new RuntimeException("Error during login.", e);
        } finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: loginAdmin");
        }
    }

    @Override
    public String deleteCity(String cityName) {
        try {
            logger.info("START :: CLASS :: TourServiceImpl :: METHOD :: deleteCity");
            TourValidation.validCityName(cityName);
            City city = cityRepository.getIdByCityName(cityName);
            if (city == null) {
                throw new CityNotFoundException(cityName + " does not exist in the database");
            } else {
                cityRepository.deleteById(city.getId());
                return cityName + " is deleted from your database";
            }
        } catch (InvalidFormatException e) {
            logger.error("Invalid Format Exception");
            e.printStackTrace();
            throw e;
        } catch (CityNotFoundException e) {
            logger.error("City Not Found Exception");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting city: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("An error occurred while deleting " + cityName, e);
        }finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: deleteCity");
        }
    }

    @Override
    public TourInfo updateTourInfo(int tourInfoId, TourRequestDto updatedTour) {
        try {
            logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: updateTourInfo");
            TourInfo tourInfo = tourInfoRepository.getById(tourInfoId);
            City sourceCityByTourInfoId = tourInfoRepository.getSourceCityByTourInfoId(tourInfoId);
            City destinationCityByTourInfoId = tourInfoRepository.getDestinationCityByTourInfoId(tourInfoId);
            BusType busTypeByTourInfoId = tourInfoRepository.getBusTypeByTourInfoId(tourInfoId);
            Tour tour = tourRepository.getBySourceCityAndDestinationCityAndBusType(sourceCityByTourInfoId, destinationCityByTourInfoId, busTypeByTourInfoId);
            if (tourInfo == null || tour == null) {
                throw new EntityNotFoundException("This tour is not present in our database");
            }
            City sourceCity = cityRepository.findByCityName(updatedTour.getSourceCity());
            if (!cityRepository.existsByCityName(updatedTour.getSourceCity())) {
                throw new CityNotFoundException("This city is not present in our list....");
            }
            if (updatedTour.getSourceCity() != null) {
                TourValidation.validCityName(updatedTour.getSourceCity());
                tourInfo.setSourceCity(sourceCity);
                tour.setSourceCity(sourceCity);
            }
            City destinationCity = cityRepository.findByCityName(updatedTour.getDestinationCity());
            if (!cityRepository.existsByCityName(updatedTour.getDestinationCity())) {
                throw new CityNotFoundException("This city is not present in our list....");
            }
            if (updatedTour.getDestinationCity() != null) {
                TourValidation.validCityName(updatedTour.getDestinationCity());
                tourInfo.setDestinationCity(destinationCity);
                tour.setDestinationCity(destinationCity);
            }
            if (updatedTour.getBusType() != null) {
                tourInfo.setBusType(BusType.valueOf(updatedTour.getBusType()));
                tour.setBusType(BusType.valueOf(updatedTour.getBusType()));
            }
            if (updatedTour.getTravelDurationMinutes() != null) {
                if (updatedTour.getTravelDurationMinutes() > 0) {
                    tourInfo.setTravelDurationMinutes(updatedTour.getTravelDurationMinutes());
                    tour.setDepartureTime(TourValidation.getDepartureTime(updatedTour.getTravelDurationMinutes(), tour.getArrivalTime()));
                } else {
                    throw new NullValueException("Travel duration can not be zero");
                }
            }
            if (updatedTour.getCost() != null) {
                if (updatedTour.getCost() > 0) {
                    tourInfo.setCost(updatedTour.getCost());
                    tour.setCost(updatedTour.getCost());
                } else {
                    throw new NullValueException("Cost can not be zero");
                }
            }
            if (updatedTour.getStatus() != null) {
                tourInfo.setStatus(updatedTour.getStatus());
            }
            tourRepository.save(tour);
            return tourInfoRepository.save(tourInfo);
        } catch (EntityNotFoundException e) {
            logger.error("Tour Not Found Exception");
            throw e;
        } catch (CityNotFoundException e) {
            logger.error("City Not Found Exception");
            throw e;
        } catch (InvalidFormatException e) {
            logger.error("Invalid Format Exception");
            throw e;
        } catch (NullValueException e) {
            logger.error("Null Value Exception");
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred while updating tour info", e);
            throw new RuntimeException("Failed to update tour info", e);
        }finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: updateTourInfo");
        }
    }

    @Override
    public String cancelTour(int tourInfoId) {
        try {
            logger.info("START :: CLASS :: AdminServiceImpl :: METHOD :: cancelTour");
            TourInfo tourInfo = tourInfoRepository.getById(tourInfoId);
            if (tourInfo == null) {
                throw new EntityNotFoundException("This tour is not present in tour info database...");
            }
            tourInfo.setStatus(CANCEL);
            City sourceCityByTourInfoId = tourInfoRepository.getSourceCityByTourInfoId(tourInfoId);
            City destinationCityByTourInfoId = tourInfoRepository.getDestinationCityByTourInfoId(tourInfoId);
            BusType busTypeByTourInfoId = tourInfoRepository.getBusTypeByTourInfoId(tourInfoId);
            Tour tour = tourRepository.getBySourceCityAndDestinationCityAndBusType(sourceCityByTourInfoId, destinationCityByTourInfoId, busTypeByTourInfoId);
            if (tour == null) {
                throw new EntityNotFoundException("This tour is not present in tour database...");
            }
            tourRepository.deleteById(tour.getTourId());
            tourInfoRepository.save(tourInfo);
            return "Tour canceled successfully !";
        } catch (EntityNotFoundException e) {
            logger.error("Tour Not Found Exception");
            throw e;
        }
        catch (Exception e) {
            logger.error("An error occurred while updating tour info", e);
            throw new RuntimeException("Failed to update tour info", e);
        }finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: cancelTour");
        }
    }
}