package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.RidingRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RidingRecordPostDto {
    private Long bicycleId;
    private Long ridingTime;
    private Double distance;
    private Double avgSpeed;
    private Double maxSpeed;
    private Long ridingDuration;
    private String userList;
    //no map


    public RidingRecord toEntity(String map) {
        RidingRecord ridingRecord = new RidingRecord();
        ridingRecord.setBicycleId(bicycleId);
        ridingRecord.setRidingTime(ridingTime);
        ridingRecord.setDistance(distance);
        ridingRecord.setAvgSpeed(avgSpeed);
        ridingRecord.setMaxSpeed(maxSpeed);
        ridingRecord.setRidingDuration(ridingDuration);
        ridingRecord.setUserList(userList);
        ridingRecord.setMap(map);
        System.out.println("toEntity : " + ridingRecord.getMap());
        return ridingRecord;
    }
}
