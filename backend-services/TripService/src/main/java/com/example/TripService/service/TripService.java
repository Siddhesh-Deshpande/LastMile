package com.example.TripService.service;


import com.example.TripService.entity.Trip;
import com.example.TripService.repository.TripRepository;
import com.example.kafkaevents.events.MatchingEvent;
import com.example.kafkaevents.events.NotifyPartiesEvent;
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
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics="trip-service")
    public void listen(MatchingEvent matchingEvent, Acknowledgment ack)
    {
        System.out.println("Received Matching Event: " + matchingEvent);
        Trip t = new Trip(matchingEvent.getRiderId(), matchingEvent.getDriverId(), matchingEvent.getArrivalId(), matchingEvent.getArrivalstationname());
        Trip databaseTrip = tripRepository.save(t);
        kafkaTemplate.send("rider-service", new UpdateStatusEvent(matchingEvent.getArrivalId(), "SCHEDULED"));
        kafkaTemplate.send("notification-service",new NotifyPartiesEvent(databaseTrip.getRiderId(),databaseTrip.getDriverId(),databaseTrip.getTripId()));
        ack.acknowledge();
    }



}
