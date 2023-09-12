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

    String memberId;
    Integer bicycleNo;
    Integer tire;
    Integer brakes;
    Integer chain;
    Integer gears;
    Long managementTime;

    public ManagementRecord toEntity() {
        ManagementRecord managementRecord = new ManagementRecord();
        managementRecord.setMemberId(memberId);
        managementRecord.setBicycleNo(bicycleNo);
        managementRecord.setTire(tire);
        managementRecord.setBrakes(brakes);
        managementRecord.setChain(chain);
        managementRecord.setGears(gears);
        managementRecord.setManagementTime(managementTime);
        return managementRecord;
    }

}
