package com.sba.recordingserver.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ManagementRecordInfoDto {
    private List<ManagementRecordSimplifiedDto> records;
    private BicycleStatusDto bicycleStatus;

}
