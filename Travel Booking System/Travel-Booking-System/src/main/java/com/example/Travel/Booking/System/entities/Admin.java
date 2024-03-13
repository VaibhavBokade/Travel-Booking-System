package com.example.Travel.Booking.System.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userName", length = 30)
    private String userName;

    @Column(name = "password", length = 50)
    private String password;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<TourInfo> tourInfoList;
}
