package com.example.test.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private String driverName;

    private String phoneNumber;

    private String vehicleNumber;

    private String vehicleType;

    private String route;

    private Integer capacity;
}