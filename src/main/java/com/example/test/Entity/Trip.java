package com.example.test.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    private String pickupLocation;
    private String dropLocation;
    private Double totalFare;
    private Integer occupancyCount;
    private String tripStatus;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "trip_riders",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "rider_id")
    )
    private Set<Rider> riders = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

}