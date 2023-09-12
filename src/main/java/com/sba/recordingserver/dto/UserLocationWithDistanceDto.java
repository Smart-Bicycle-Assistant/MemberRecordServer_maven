package com.sba.recordingserver.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserLocationWithDistanceDto {
    private String id;
    private Double longitude;
    private Double latitude;
    private Double distance;
}
