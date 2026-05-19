package com.example.test.Service;

import com.example.test.Entity.Rider;
import com.example.test.Repo.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository Repo;

    public Rider addRider(Rider rider) {
        return Repo.save(rider);
    }

    public List<Rider> getAllRiders() {
        return Repo.findAll();
    }

    public void deleteRider(Long id) {
        Repo.deleteById(id);
    }
}