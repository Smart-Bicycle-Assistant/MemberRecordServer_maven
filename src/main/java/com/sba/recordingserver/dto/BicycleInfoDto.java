package com.sba.recordingserver.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BicycleInfoDto {
    private Long bicycleId;
    private String bicycleName;
    private String bicycleImage;
    private Long registerTime;
    private Double distance;
}
