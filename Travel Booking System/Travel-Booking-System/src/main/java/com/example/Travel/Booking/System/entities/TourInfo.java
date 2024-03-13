package com.example.Travel.Booking.System.entities;

import com.example.Travel.Booking.System.enums.BusType;
import com.example.Travel.Booking.System.enums.Status;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class TourInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_city_id")
    private City sourceCity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_city_id")
    private City destinationCity;

    @Enumerated(EnumType.STRING)
    @Column(name = "bus_type", length = 20)
    private BusType busType;

    @Column(name = "travel_duration_minutes")
    private Long travelDurationMinutes;

    @Column(name = "cost")
    private double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public TourInfo() {
        this.status = Status.NOT_COMPLETED;
    }
}
