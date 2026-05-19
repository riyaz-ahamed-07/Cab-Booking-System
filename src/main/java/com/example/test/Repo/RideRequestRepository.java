package com.example.test.Repo;

import com.example.test.Entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    List<RideRequest> findByRequestStatus(String requestStatus);
}