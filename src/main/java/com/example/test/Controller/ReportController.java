package com.example.test.Controller;

import com.example.test.Entity.Trip;
import com.example.test.Service.RidePoolingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final RidePoolingService ridePoolingService;

    @GetMapping("/average-occupancy")
    public Map<String, Object> getAverageOccupancy() {
        Map<String, Object> result = new HashMap<>();
        result.put("averageOccupancy", ridePoolingService.calculateAverageOccupancy());
        return result;
    }

    @GetMapping("/most-profitable-trips")
    public List<Trip> getMostProfitableTrips(@RequestParam(defaultValue = "5") int limit) {
        return ridePoolingService.findMostProfitableTrips(limit);
    }
}