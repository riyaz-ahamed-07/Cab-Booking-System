package com.example.test.Controller;

import com.example.test.Entity.RideRequest;
import com.example.test.Entity.Trip;
import com.example.test.Service.RidePoolingService;
import com.example.test.Service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ride-requests")
@RequiredArgsConstructor
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final RidePoolingService ridePoolingService;

    @PostMapping
    public RideRequest createRequest(@RequestBody RideRequest rideRequest) {
        return rideRequestService.addRideRequest(rideRequest);
    }

    @GetMapping
    public List<RideRequest> getAllRequests() {
        return rideRequestService.getAllRequests();
    }

    @GetMapping("/pending")
    public List<RideRequest> getPendingRequests() {
        return rideRequestService.getPendingRequests();
    }

    @PostMapping("/{id}/match")
    public Trip matchRequest(@PathVariable Long id) {
        return ridePoolingService.matchAndCreatePool(id);
    }

    @DeleteMapping("/{id}")
    public String deleteRequest(@PathVariable Long id) {
        rideRequestService.deleteRequest(id);
        return "Ride request deleted successfully";
    }
}