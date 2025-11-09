package com.example.kafkaevents.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderDataRedis {
    private Integer arrivalId;
    private LocalDateTime arrivaltime;
    private String destination;
    private String arrivalstationname;
}
