package com.example.RiderService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalRegistrationDTO {
    private LocalDateTime arrivaltime;
    private String destination;
    private String arrivalstationname;
}

