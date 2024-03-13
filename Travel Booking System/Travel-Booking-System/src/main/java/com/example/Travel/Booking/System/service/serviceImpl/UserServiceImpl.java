package com.example.Travel.Booking.System.service.serviceImpl;

import com.example.Travel.Booking.System.dto.LoginDto;
import com.example.Travel.Booking.System.entities.Tour;
import com.example.Travel.Booking.System.entities.User;
import com.example.Travel.Booking.System.entities.UserTour;
import com.example.Travel.Booking.System.exception.*;
import com.example.Travel.Booking.System.mailSending.Notification;
import com.example.Travel.Booking.System.repository.TourRepository;
import com.example.Travel.Booking.System.repository.UserRepository;
import com.example.Travel.Booking.System.service.UserService;
import com.example.Travel.Booking.System.validation.UserValidation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public User registerUser(User user) {
        try {
            logger.info("START :: CLASS :: UserServiceImpl :: METHOD :: registerUser");
            boolean validEmail = UserValidation.isValidEmail(user.getEmail());
            if (!validEmail) {
                throw new EmailValidationException("Enter valid email....");
            }
            if (userRepository.existsById(user.getEmail())) {
                throw new UserAlreadyExistException("User with email " + user.getEmail() + " already exists!");
            }
            User validUser = new User();
            String number = UserValidation.validateMobileNumber(user.getMobileNumber());
            String password = UserValidation.validatePassword(user.getPassword());
            if (userRepository.existsByPassword(password)) {
                throw new PasswordAlreadyExistException("Password already exists for another user");
            }
            validUser.setEmail(user.getEmail());
            validUser.setFirstName(user.getFirstName());
            validUser.setLastName(user.getLastName());
            validUser.setMobileNumber(number);
            String encryptedPassword = encryptionService.encryptPassword(user.getPassword());
            validUser.setPassword(encryptedPassword);
            userRepository.save(validUser);
            Notification.sendRegistrationEmail(user.getFirstName(), user.getLastName(), validUser.getEmail());
            return validUser;
        } catch (UserAlreadyExistException | PasswordAlreadyExistException | InvalidUserException |
                 InvalidMobileNumberException | InvalidPasswordException | EmailValidationException e) {
            logger.error("User not found Exception");
            e.printStackTrace();
            throw e;
        } catch (RuntimeException e) {
            logger.error("An error occurred during adding user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: registerUser");
        }
    }

    @Override
    public User loginUser(LoginDto loginDto) {
        try {
            logger.info("START :: CLASS :: UserServiceImpl :: METHOD :: loginUser");
            User user = userRepository.findByEmail(loginDto.getEmail());
            if (user != null && encryptionService.checkPassword(loginDto.getPassword(), user.getPassword())) {
                return user;
            } else {
                throw new UserNotFoundException("Invalid Username / Password");
            }
        } catch (UserNotFoundException e) {
            logger.error("User not found Exception");
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred during login: " + e.getMessage());
            throw new RuntimeException("Error during login.", e);
        } finally {
            logger.info("END :: CLASS :: UserServiceImpl :: METHOD :: loginUser");
        }
    }

//    @Override
//    public User bookTour(TourBookingRequest tourBookingRequest) {
//        try {
//            logger.info("START :: CLASS :: UserServiceImpl :: METHOD :: bookTour");
//            String userEmail = tourBookingRequest.getUserEmail();
//            String source = tourBookingRequest.getSource();
//            String destination = tourBookingRequest.getDestination();
//            double price = tourBookingRequest.getPrice();
//            LocalDate arrangeDate = tourBookingRequest.getArrangeDate();
//            User user = userRepository.findByEmail(userEmail);
//            if (user == null) {
//                throw new UserNotFoundException("User with email " + userEmail + " not found");
//            }
//            Tour existingTour = tourRepository.findBySourceAndDestinationAndPriceAndArrangeDate(source, destination, price, arrangeDate);
//            if (existingTour == null) {
//                throw new TourNotExistException("Tour does not exist");
//            }
//            UserTour userTour = new UserTour();
//            userTour.setUser(user);
//            userTour.setTour(existingTour);
//            List<UserTour> userTours = user.getUserTours();
//            if (userTours == null) {
//                userTours = new ArrayList<>();
//            }
//            userTours.add(userTour);
//            user.setUserTours(userTours);
//            User savedUser = userRepository.save(user);
//            return savedUser;
//        } catch (UserNotFoundException e) {
//            logger.error("User Not Found Exception", e);
//            throw e;
//        } catch (TourNotExistException e) {
//            logger.error("Tour not found Exception", e);
//            throw e;
//        } catch (Exception e) {
//            logger.error("Error during tour booking.", e);
//            throw new RuntimeException("Error during tour booking.", e);
//        }
//    }

    @Override
    public void cancelBooking(String userEmail, int tourId) {
        try {
            logger.info("START :: CLASS :: UserServiceImpl :: METHOD :: cancelBooking");
            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                throw new UserNotFoundException("User with email " + userEmail + " not found");
            } else if (StringUtils.isBlank(userEmail)) {
                throw new NullValueException("Please give your credentials !");
            }
            Tour tour = tourRepository.findById(tourId)
                    .orElseThrow(() -> new TourNotFoundException("Tour with id " + tourId + " not found"));

            UserTour userTourToRemove = null;
            for (UserTour userTour : user.getUserTours()) {
                if (userTour.getTour().getTourId() == tourId) {
                    userTourToRemove = userTour;
                    break;
                }
            }
            if (userTourToRemove == null) {
                throw new BookingNotFoundException("Booking not found for user " + userEmail + " and tour " + tourId);
            }
            user.getUserTours().remove(userTourToRemove);
            userRepository.save(user);
        } catch (UserNotFoundException | TourNotFoundException | BookingNotFoundException e) {
            logger.error("Exception occurred", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error canceling booking.", e);
            throw new RuntimeException("Error canceling booking.", e);
        }
    }
}
