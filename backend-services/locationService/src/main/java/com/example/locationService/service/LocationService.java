package com.example.locationService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import com.example.locationService.grpc.StationServiceGrpc.StationServiceImplBase;
import com.example.locationService.grpc.isNearby;
import com.example.locationService.grpc.stationNameAndLocation;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

@GrpcService
public class LocationService extends StationServiceImplBase{
    @Autowired
    private stationServiceGrpcClient SSclient;

    @Override
    public void checkIfNearby(stationNameAndLocation request,StreamObserver<isNearby> responseObserver){
        String stationNameStr = request.getStationName();
        String driverCurrLoc = request.getDriverLocation();
        ArrayList<String> nearbyLocations = SSclient.getAllNearbyAreas(stationNameStr);
        boolean ifNearby = false;
        if(nearbyLocations.contains(driverCurrLoc)){
            ifNearby = true;
        }
        isNearby retval = isNearby.newBuilder()
            .setNearby(ifNearby)
            .build();
        responseObserver.onNext(retval);
        responseObserver.onCompleted();
    }

}
