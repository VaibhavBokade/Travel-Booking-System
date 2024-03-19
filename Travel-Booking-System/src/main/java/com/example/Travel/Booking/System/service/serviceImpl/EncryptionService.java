package com.example.Travel.Booking.System.service.serviceImpl;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

    public String encryptPassword(String password) {
        return passwordEncryptor.encryptPassword(password);
    }

    public boolean checkPassword(String inputPassword, String encryptedPassword) {
        return passwordEncryptor.checkPassword(inputPassword, encryptedPassword);
    }
}
