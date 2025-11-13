package com.example.TripService.controller;

import com.example.TripService.dtos.RideActiveConfirmationDTO;
import com.example.TripService.entity.Trip;
import com.example.TripService.repository.TripRepository;
import com.example.kafkaevents.events.UpdateStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TripController {
    //TODO: Implement trip-related endpoints(For Confirming from rider for trip started basically the next step of scheduled i.e active)
    // Trip DB chnage status
    //send event to kafka to rider service topic so change the status of ride of a specific arribalid
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @PostMapping("/confirmTrip")
    public ResponseEntity<?> confirmTrip(@RequestBody RideActiveConfirmationDTO rideActiveConfirmationDTO) {
        Integer tripId = rideActiveConfirmationDTO.getTripId();
        Trip trip = tripRepository.findById(tripId).orElse(null);
        if(trip!=null)
        {
            trip.setStatus("ACTIVE");
            tripRepository.save(trip);
            kafkaTemplate.send("rider-service",new UpdateStatusEvent(rideActiveConfirmationDTO.getArrivalId(),  "CONFIRMED"));
            return ResponseEntity.ok("Trip confirmed");
        }
        return ResponseEntity.status(404).body("Trip not found");
    }
}
