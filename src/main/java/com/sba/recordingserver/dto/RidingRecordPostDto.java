package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.RidingRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RidingRecordPostDto {
    private String memberId;
    private Integer bicycleNo;
    private Long ridingTime;
    private Double distance;
    private Double avgSpeed;
    private Double maxSpeed;
    private Long ridingDuration;
    private String map;
    public RidingRecord toEntity() {
        RidingRecord ridingRecord = new RidingRecord();
        ridingRecord.setMemberId(memberId);
        ridingRecord.setBicycleNo(bicycleNo);
        ridingRecord.setRidingTime(ridingTime);
        ridingRecord.setDistance(distance);
        ridingRecord.setAvgSpeed(avgSpeed);
        ridingRecord.setMaxSpeed(maxSpeed);
        ridingRecord.setRidingDuration(ridingDuration);
        ridingRecord.setMap(map);
        return ridingRecord;
    }
}
