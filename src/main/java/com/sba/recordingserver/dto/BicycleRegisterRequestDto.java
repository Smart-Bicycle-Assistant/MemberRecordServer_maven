package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.Bicycle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BicycleRegisterRequestDto {
    private String bicycleName;
    private String bicycleImage;
    private Long registerTime;

    public Bicycle toEntity() {
        Bicycle bicycle = new Bicycle();
        bicycle.setBicycleName(bicycleName);
        bicycle.setBicycleImage(bicycleImage);
        bicycle.setRegisterTime(registerTime);
        return bicycle;
    }
}
