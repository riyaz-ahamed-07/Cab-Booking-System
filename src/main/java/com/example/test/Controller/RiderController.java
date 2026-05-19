package com.example.test.Controller;

import com.example.test.Entity.Rider;
import com.example.test.Service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public Rider addRider(@RequestBody Rider rider) {
        return riderService.addRider(rider);
    }

    @GetMapping
    public List<Rider> getAllRiders() {
        return riderService.getAllRiders();
    }

    @DeleteMapping("/{id}")
    public String deleteRider(@PathVariable Long id) {
        riderService.deleteRider(id);
        return "Rider deleted successfully";
    }
}