package com.example.TripService.repository;

import com.example.TripService.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface TripRepository extends JpaRepository<Trip,Integer> {
    @Query("SELECT t FROM Trip t WHERE t.driverId = ?1 AND t.arrivalstationname = ?2 AND t.status = 'SCHEDULED'")
    ArrayList<Trip> getScheduledTripsForDriver(Integer driverId, String arrivalstationname);
    @Query("select t from Trip t where t.driverId = ?1 and t.status = 'ACTIVE'")
    ArrayList<Trip> getActiveTripsForDriver(Integer driverId);
}
