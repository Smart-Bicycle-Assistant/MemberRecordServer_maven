package com.sba.recordingserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ManagementRecordSimplifiedDto {
    private Long managementTime;
    private Integer numFixed;
    private Long recordId;
}
