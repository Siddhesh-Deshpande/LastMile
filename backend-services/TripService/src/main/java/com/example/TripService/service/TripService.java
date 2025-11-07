package com.example.TripService.service;


import com.example.TripService.entity.Trip;
import com.example.TripService.repository.TripRepository;
import com.example.kafkaevents.events.MatchingEvent;
import com.example.kafkaevents.events.UpdateStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private KafkaTemplate<String, UpdateStatusEvent> kafkaTemplate;

    @KafkaListener(topics="trip-service")
    public void listen(MatchingEvent matchingEvent, Acknowledgment ack)
    {
        Trip t = new Trip(matchingEvent.getRiderId(), matchingEvent.getDriverId(), matchingEvent.getArrivalId(), matchingEvent.getArrivalstationname());
        tripRepository.save(t);
        kafkaTemplate.send("rider-service", new UpdateStatusEvent(matchingEvent.getArrivalId(), "SCHEDULED"));
        ack.acknowledge();
    }



}
