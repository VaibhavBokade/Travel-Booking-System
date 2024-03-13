package com.example.Travel.Booking.System.entities;

import com.example.Travel.Booking.System.enums.BusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tourId;

    @ManyToOne
    @JoinColumn(name = "source_city_id")
    private City sourceCity;

    @ManyToOne
    @JoinColumn(name = "destination_city_id")
    private City destinationCity;

    @Column(name = "cost")
    private double cost;

    @Column(name = "arrange_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrangeDate;

    @Column(name = "arrival_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;

    @Column(name = "departure_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "bus_type")
    private BusType busType;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserTour> userTours;
}
