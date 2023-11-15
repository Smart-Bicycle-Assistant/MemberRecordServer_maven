package com.sba.recordingserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BicycleStatusDto {
    private String bicycleName;
    private Long frontTireExchangeTime;
    private String frontTireLeftLife;
    private Long rearTireExchangeTime;
    private String rearTireLeftLife;
    private Long gearExchangeTime;
    private Long brakeExchangeTime;
    private Long chainExchangeTime;
}
