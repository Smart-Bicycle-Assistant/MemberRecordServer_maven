package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.UserLocationResultDto;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.service.RidingLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RidingLocationController {
    @Autowired
    RidingLocationService ridingLocationService;

    @GetMapping(value = "/riding_location/post_and_get")
    public ResponseDataDto<List<UserLocationResultDto>> postLocationAndGetNearbyUsers(@RequestHeader(value="Authorization")String token, @RequestParam Double longitude, @RequestParam Double latitude, @RequestParam Boolean packMode, @RequestParam Double speed)
    {

//        System.out.println("token : " + token);
//        System.out.println("memberId : " + TokenProvider.GetUserId(token.substring(token.lastIndexOf(" "))));
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingLocationService.saveLocationAndReturnNearbyUsers(memberId, longitude, latitude, packMode, speed);
    }
    @GetMapping(value = "/riding_location/startRiding")
    public ResponseNoDataDto prepareForStartRiding(@RequestHeader(value="Authorization")String token) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingLocationService.checkDirtyMemoryRepository(memberId);
    }
}
