package com.example.test.Service;

import com.example.test.Entity.Driver;
import com.example.test.Repo.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository Repo;

    public Driver addDriver(Driver driver) {
        return Repo.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return Repo.findAll();
    }

    public void deleteDriver(Long id) {
        Repo.deleteById(id);
    }
}