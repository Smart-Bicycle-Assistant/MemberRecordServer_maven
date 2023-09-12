package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.UserLocationDto;
import com.sba.recordingserver.dto.UserLocationWithDistanceDto;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.service.RidingLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RidingLocationController {
    @Autowired
    RidingLocationService ridingLocationService;

    @GetMapping(value = "/riding_location/post_and_get")
    public ResponseDataDto<List<UserLocationDto>> postLocationAndGetNearbyUsers(@RequestParam String memberId, @RequestParam Double longitude, @RequestParam Double latitude)
    {
        return ridingLocationService.saveLocationAndReturnNearbyUsers(memberId, longitude, latitude);
    }
}
