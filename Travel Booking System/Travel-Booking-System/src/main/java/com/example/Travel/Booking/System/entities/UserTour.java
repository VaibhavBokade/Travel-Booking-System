package com.example.Travel.Booking.System.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_tour")
public class UserTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_id", referencedColumnName = "tourId")
    private Tour tour;

}
