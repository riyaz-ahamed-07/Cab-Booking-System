package com.example.test.Service;

import com.example.test.Entity.Driver;
import com.example.test.Entity.RideRequest;
import com.example.test.Entity.Trip;
import com.example.test.Repo.DriverRepository;
import com.example.test.Repo.RideRequestRepository;
import com.example.test.Repo.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RidePoolingService {

    private static final double FARE_PER_KM = 12.0;
    private final RideRequestRepository rideRequestRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final RouteGraph routeGraph;

    public RideRequest submitRequest(RideRequest rideRequest) {
        return rideRequestRepository.save(rideRequest);
    }

    public Trip matchAndCreatePool(Long requestId) {
        return matchAndCreatePool(requestId, null);
    }

    public Trip matchAndCreatePool(Long requestId, Long driverId) {
        RideRequest baseRequest = rideRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Ride request not found: " + requestId));

        if (!"PENDING".equalsIgnoreCase(baseRequest.getRequestStatus())) {
            throw new IllegalStateException("Ride request is not available for matching: " + requestId);
        }

        List<RideRequest> candidates = rideRequestRepository.findByRequestStatus("PENDING").stream()
                .filter(request -> !request.getRequestId().equals(requestId))
                .filter(request -> routeGraph.areRoutesSimilar(
                        baseRequest.getPickupLocation(),
                        baseRequest.getDropLocation(),
                        request.getPickupLocation(),
                        request.getDropLocation()))
                .sorted(Comparator.comparingDouble(request -> routeGraph.computeRouteDistance(
                        baseRequest.getPickupLocation(), request.getPickupLocation()) +
                        routeGraph.computeRouteDistance(baseRequest.getDropLocation(), request.getDropLocation())))
                .collect(Collectors.toList());

        List<RideRequest> pool = new ArrayList<>();
        pool.add(baseRequest);

        for (RideRequest candidate : candidates) {
            int expectedOccupancy = pool.size() + 1;
            if (!hasDriverForCapacity(expectedOccupancy)) {
                break;
            }
            pool.add(candidate);
        }

        Driver driver = driverId == null ? selectDriver(pool.size()) : driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        if (driverId != null) {
            if (driver.getCapacity() == null || driver.getCapacity() < pool.size()) {
                throw new IllegalStateException("Selected driver does not have enough capacity.");
            }
            if (!routeGraph.coversPickupDrop(driver.getRoute(), baseRequest.getPickupLocation(), baseRequest.getDropLocation())) {
                throw new IllegalStateException("Selected driver route does not cover the requested pickup and drop locations.");
            }
        }

        double tripDistance = routeGraph.computeRouteDistance(baseRequest.getPickupLocation(), baseRequest.getDropLocation());
        double totalFare = Math.max(5.0, tripDistance) * FARE_PER_KM;

        Trip trip = new Trip();
        trip.setPickupLocation(baseRequest.getPickupLocation());
        trip.setDropLocation(baseRequest.getDropLocation());
        trip.setDriver(driver);
        trip.setOccupancyCount(pool.size());
        trip.setTotalFare(totalFare);
        trip.setTripStatus("ASSIGNED");

        Set<com.example.test.Entity.Rider> riders = pool.stream()
                .map(RideRequest::getRider)
                .collect(Collectors.toSet());
        trip.setRiders(riders);

        Trip savedTrip = tripRepository.save(trip);

        pool.forEach(request -> {
            request.setRequestStatus("MATCHED");
            rideRequestRepository.save(request);
        });

        return savedTrip;
    }

    public java.util.List<Driver> findDriversForRoute(String pickupLocation, String dropLocation) {
        return driverRepository.findAll().stream()
                .filter(driver -> driver.getRoute() != null)
                .filter(driver -> routeGraph.coversPickupDrop(driver.getRoute(), pickupLocation, dropLocation))
                .toList();
    }

    private boolean hasDriverForCapacity(int expectedOccupancy) {
        return driverRepository.findAll().stream()
                .anyMatch(driver -> driver.getCapacity() != null && driver.getCapacity() >= expectedOccupancy);
    }

    private Driver selectDriver(int occupancy) {
        return driverRepository.findAll().stream()
                .filter(driver -> driver.getCapacity() != null && driver.getCapacity() >= occupancy)
                .min(Comparator.comparingInt(Driver::getCapacity))
                .orElseThrow(() -> new IllegalStateException("No driver available for occupancy " + occupancy));
    }

    public Trip completeTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));
        trip.setTripStatus("COMPLETED");
        return tripRepository.save(trip);
    }

    public double calculateAverageOccupancy() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
                .mapToInt(Trip::getOccupancyCount)
                .average()
                .orElse(0.0);
    }

    public List<Trip> findMostProfitableTrips(int limit) {
        return tripRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Trip::getTotalFare).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}