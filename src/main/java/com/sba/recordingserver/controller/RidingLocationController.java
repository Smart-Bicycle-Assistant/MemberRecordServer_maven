package com.sba.recordingserver.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.UserLocationResultDto;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.service.RidingLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class RidingLocationController {
    @Autowired
    RidingLocationService ridingLocationService;

    @GetMapping(value = "/riding_location/post_and_get")
    public ResponseDataDto<List<UserLocationResultDto>> postLocationAndGetNearbyUsers(@RequestHeader(value="Authorization")String token, @RequestParam Double longitude, @RequestParam Double latitude, @RequestParam Boolean packMode, @RequestParam Double targetSpeed, @RequestParam Double curSpeed)
    {

//        System.out.println("token : " + token);
//        System.out.println("memberId : " + TokenProvider.GetUserId(token.substring(token.lastIndexOf(" "))));
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingLocationService.saveLocationAndReturnNearbyUsers(memberId, longitude, latitude, packMode, targetSpeed,curSpeed);
    }
    @GetMapping(value = "/riding_location/startRiding")
    public ResponseNoDataDto prepareForStartRiding(@RequestHeader(value="Authorization")String token) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingLocationService.checkDirtyMemoryRepository(memberId);
    }

    @GetMapping(value="/riding_location/ors")
    public String orsCall(@RequestParam Double startLat, @RequestParam Double startLong, @RequestParam Double destLat,@RequestParam Double destLong) {

        RestTemplate rt = new RestTemplate();
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("coordinates","[[8.681495,49.41461],[8.687872,49.420318]]");
        String body = String.format("{\"coordinates\":[[%f,%f],[%f,%f]]}",startLong,startLat,destLong,destLat);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","5b3ce3597851110001cf6248cad50f0f90ec42aa9b7241962ff718d1");
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8080/ors/v2/directions/cycling-regular", //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class
        );
        return response.getBody();
    }
}
