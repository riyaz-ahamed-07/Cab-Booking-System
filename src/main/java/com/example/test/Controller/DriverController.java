package com.example.test.Controller;

import com.example.test.Entity.Driver;
import com.example.test.Service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService ser;

    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return ser.addDriver(driver);
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return ser.getAllDrivers();
    }

    @DeleteMapping("/{id}")
    public String deleteDriver(@PathVariable Long id) {
        ser.deleteDriver(id);
        return "Driver deleted successfully";
    }
}