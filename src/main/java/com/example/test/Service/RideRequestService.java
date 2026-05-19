package com.example.test.Service;

import com.example.test.Entity.RideRequest;
import com.example.test.Repo.RideRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideRequestService {

    private final RideRequestRepository repo;

    public RideRequest addRideRequest(RideRequest rideRequest) {
        rideRequest.setRequestStatus("PENDING");
        rideRequest.setRequestTime(LocalDateTime.now());
        return repo.save(rideRequest);
    }

    public List<RideRequest> getAllRequests() {
        return repo.findAll();
    }

    public List<RideRequest> getPendingRequests() {
        return repo.findByRequestStatus("PENDING");
    }

    public RideRequest getRequestById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Ride request not found: " + id));
    }

    public void deleteRequest(Long id) {
        repo.deleteById(id);
    }
}