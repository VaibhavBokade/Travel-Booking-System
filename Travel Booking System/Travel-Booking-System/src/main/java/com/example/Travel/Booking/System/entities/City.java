package com.example.Travel.Booking.System.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "city_name", length = 20)
    private String cityName;

    @OneToMany(mappedBy = "sourceCity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TourInfo> outboundPrices = new ArrayList<>();

    @OneToMany(mappedBy = "sourceCity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tour> sourceCities = new ArrayList<>();

    @OneToMany(mappedBy = "destinationCity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tour> destinationCities = new ArrayList<>();
}
