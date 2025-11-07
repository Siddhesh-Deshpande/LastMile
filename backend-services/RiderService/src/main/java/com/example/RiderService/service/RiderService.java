package com.example.RiderService.service;

import com.example.RiderService.entity.Ride;
import com.example.RiderService.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiderService {
    @Autowired
    private RiderRepository riderRepository;

    public Ride saveRide(Ride ride) {
        return riderRepository.save(ride);
    }
    public Ride getRideById(Integer ride_id){
        return riderRepository.findById(ride_id).orElse(null);
    }
}
