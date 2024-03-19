package com.example.Travel.Booking.System.enums;

public enum BusType {
    NON_AC_PASSENGER_BUS(45),
    AC_PASSENGER_BUS(40),
    NON_AC_SLEEPER_BUS(25),
    AC_SLEEPER_BUS(20);

    private final int numberOfSeats;

    BusType(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}
