package com.sba.recordingserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RidingRecordSimplifiedDto {
    private Long ridingTime;
    private Double ridingDistance;
    private Double avgSpeed;
    private Long ridingDuration;
    private Long recordId;
}
