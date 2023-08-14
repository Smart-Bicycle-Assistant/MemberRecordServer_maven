package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.Bicycle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BicycleRegisterRequestDto {
    private String ownerId;
    private String bicycleName;
    private String bicycleImage;

    public Bicycle toEntity() {
        Bicycle bicycle = new Bicycle();
        bicycle.setOwnerId(ownerId);
        bicycle.setBicycleName(bicycleName);
        bicycle.setBicycleImage(bicycleImage);
        return bicycle;
    }
}
