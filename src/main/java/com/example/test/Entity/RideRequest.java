package com.example.test.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    private String pickupLocation;
    private String dropLocation;
    @Column(name = "request_time", columnDefinition = "DATETIME")
    private LocalDateTime requestTime;
    private String requestStatus;
}