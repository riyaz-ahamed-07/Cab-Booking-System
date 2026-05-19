package com.example.test.Service;

import com.example.test.Entity.Trip;
import com.example.test.Repo.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository repo;

    public Trip createTrip(Trip trip) {
        return repo.save(trip);
    }

    public List<Trip> getAllTrips() {
        return repo.findAll();
    }

    public void deleteTrip(Long id) {
        repo.deleteById(id);
    }

    public Trip completeTrip(Long id) {
        Trip trip = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Trip not found: " + id));
        trip.setTripStatus("COMPLETED");
        return repo.save(trip);
    }
}