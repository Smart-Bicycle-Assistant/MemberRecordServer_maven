package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.ManagementRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ManagementRecordPostDto {
//    aiaiaiaiaiaiaiaiaiaiai aiai

    Long bicycleId;
    Integer frontTire;
    Integer rearTire;
    Integer frontTireLife;
    Integer rearTireLife;
    Integer brakes;
    Integer chain;
    Integer gears;
    Long managementTime;

    public ManagementRecord toEntity() {
        ManagementRecord managementRecord = new ManagementRecord();
        managementRecord.setBicycleId(bicycleId);
        managementRecord.setFrontTire(frontTire);
        managementRecord.setFrontTireLife(frontTireLife);
        managementRecord.setRearTire(rearTire);
        managementRecord.setRearTireLife(rearTireLife);
        managementRecord.setBrakes(brakes);
        managementRecord.setChain(chain);
        managementRecord.setGears(gears);
        managementRecord.setManagementTime(managementTime);
        return managementRecord;
    }

}
