package com.example.MatchingService.scheduler;

import com.example.MatchingService.config.RedisLockManager;

import com.example.kafkaevents.events.DriverDataRedis;
import com.example.kafkaevents.events.RiderDataRedis;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@Service
public class MatchingScheduler {

    private RedisLockManager lockManager;
    private RedisTemplate<String, DriverDataRedis> redisTemplateForDriver;
    private RedisTemplate<String, RiderDataRedis> redisTemplateForRider;
    private final String instanceId = UUID.randomUUID().toString(); // unique per instance
    private static final String LOCK_KEY = "locks:job-runner";
    private Integer window_size = 5;
    public MatchingScheduler (RedisLockManager lockManager, RedisTemplate<String, DriverDataRedis> redisTemplateForDriver, RedisTemplate<String, RiderDataRedis> redisTemplateForRider) {
        this.lockManager = lockManager;
        this.redisTemplateForDriver = redisTemplateForDriver;
        this.redisTemplateForRider = redisTemplateForRider;
    }
    private <T> HashMap<String,T> getDataFromRedis(String matchkey,RedisTemplate<String,T> redisTemplate)
    {
        ScanOptions options = ScanOptions.scanOptions()
                .match(matchkey)
                .count(100)
                .build();
        HashMap<String,T> resultMap = new HashMap<>();

        try (Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.executeWithStickyConnection(
                conn -> conn.keyCommands().scan(options))) {

            while (cursor != null && cursor.hasNext()) {
                String key = new String(cursor.next());
                T value =  redisTemplate.opsForValue().get(key);
                resultMap.put(key, value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    private void match(HashMap<String, DriverDataRedis> driverdata, HashMap<String, RiderDataRedis> riderdata)
    {
//        for(String dkey: driverdata.keySet())
//        {
//            DriverDataRedis driverData = driverdata.get(dkey);
//            for(String rkey: riderdata.keySet())
//            {
//                //Matching Logic
//                    RiderDataRedis riderData = riderdata.get(rkey);
//                stationNameAndLocation stationNameAndLoc = stationNameAndLocation.newBuilder()
//                        .setStationName(riderData.getArrivalstationname())
//                        .setDriverLocation(driverData.getCurrentLocation())
//                        .build();
//                if(driverData.getDestination().equals(riderData.getDestination())
//                        && driverData.getAvailableSeats()>0
//                        && Duration.between(LocalDateTime.now(), riderData.getArrivaltime()).toMinutes()<=window_size
//                        &&
//
//
//
//                )
//            }
//        }
    }
    @Scheduled(fixedRate = 5000)//Fix the Time Required
    public void runPeriodicJob() {
        HashMap<String, DriverDataRedis> driverdata;
        HashMap<String, RiderDataRedis> riderdata;
        boolean acquired = lockManager.tryAcquireLock(LOCK_KEY, instanceId, Duration.ofSeconds(10));
        if (!acquired) {
            // Some other instance is running it
            return;
        }

        try {
            driverdata = getDataFromRedis("driver-service:driver:*:route", redisTemplateForDriver);
            riderdata = getDataFromRedis("rider-service:rider:*:arrival", redisTemplateForRider);


        } finally {
            lockManager.releaseLock(LOCK_KEY, instanceId);
        }
    }

}
