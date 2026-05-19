package com.example.test.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "riders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;

    private String riderName;
    private String phoneNumber;
    private String email;
    private String pickupLocation;
    private String dropLocation;
}