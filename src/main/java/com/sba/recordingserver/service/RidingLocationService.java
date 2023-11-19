package com.sba.recordingserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.UserLocationResultDto;
import com.sba.recordingserver.entity.RidingCoordinate;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.entity.RidingSpeed;
import com.sba.recordingserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RidingLocationService {
    @Autowired
    RidingLocationRepository ridingLocationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RidingCoordinateRepository ridingCoordinateRepository;
    @Autowired
    RidingSpeedRepository ridingSpeedRepository;
//    RidingCoordinateMemoryRepository ridingCoordinateMemoryRepository = RidingCoordinateMemoryRepository.getInstance();
//    RidingSpeedMemoryRepository ridingSpeedMemoryRepository = RidingSpeedMemoryRepository.getInstance();
    public ResponseDataDto<List<UserLocationResultDto>> saveLocationAndReturnNearbyUsers(String memberId, Double longitude, Double latitude, Boolean packMode, Double targetSpeed, Double curSpeed)
    {
        Optional<RidingCoordinate> ridingCoordinate =  ridingCoordinateRepository.findById(memberId);
        if(ridingCoordinate.isEmpty())
        {
            RidingCoordinate newRidingCoordinate = new RidingCoordinate();
            newRidingCoordinate.setId(memberId);

            List<Double> listLongitude = new ArrayList<>();
            listLongitude.add(longitude);
            newRidingCoordinate.setLongitude(new Gson().toJson(listLongitude) );

            List<Double> listLatitude = new ArrayList<>();
            listLatitude.add(latitude);
            newRidingCoordinate.setLatitude(new Gson().toJson(listLatitude));
            while(listLongitude.size() < listLatitude.size()) {
                listLongitude.add(listLongitude.get(listLongitude.size() -1));
            }
            while(listLongitude.size() > listLatitude.size()) {
                listLatitude.add(listLatitude.get(listLatitude.size() -1));
            }
            ridingCoordinateRepository.save(newRidingCoordinate);
        }
        else {
            RidingCoordinate thisCoordinate = ridingCoordinate.get();
            String latitudes = thisCoordinate.getLatitude();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Double> listLatitude = objectMapper.readValue(latitudes, new TypeReference<List<Double>>() {
                });
                listLatitude.add(latitude);
                thisCoordinate.setLatitude(new Gson().toJson(listLatitude));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String longitudes = thisCoordinate.getLongitude();
            try {
                List<Double> listLongitude = objectMapper.readValue(longitudes, new TypeReference<List<Double>>() {
                });
                listLongitude.add(longitude);
                thisCoordinate.setLongitude(new Gson().toJson(listLongitude));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ridingCoordinateRepository.save(thisCoordinate);
        }

        if(ridingSpeedRepository.findById(memberId).isEmpty()) {
            RidingSpeed newRidingSpeed = new RidingSpeed();
            newRidingSpeed.setId(memberId);
            List<Double> listSpeed = new ArrayList<>();
            listSpeed.add(curSpeed);
            newRidingSpeed.setSpeed(new Gson().toJson(listSpeed));
            ridingSpeedRepository.save(newRidingSpeed);
        }
        else {
            RidingSpeed thisRidingSpeed = ridingSpeedRepository.findById(memberId).get();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Double> listSpeed = objectMapper.readValue(thisRidingSpeed.getSpeed(), new TypeReference<List<Double>>() {
                });
                listSpeed.add(curSpeed);
                thisRidingSpeed.setSpeed(new Gson().toJson(listSpeed));
                ridingSpeedRepository.save(thisRidingSpeed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        System.out.println(latitudes);
//        ridingCoordinateMemoryRepository.save(memberId,longitude,latitude);
//        ridingSpeedMemoryRepository.save(memberId,curSpeed);

        Optional<RidingLocation> optionalRidingLocation = ridingLocationRepository.findById(memberId);
        if(optionalRidingLocation.isPresent())
        {
            RidingLocation ridingLocation = optionalRidingLocation.get();
            ridingLocation.setLongitude(longitude);
            ridingLocation.setLatitude(latitude);
            ridingLocationRepository.save(ridingLocation);
        }
        else
        {
            RidingLocation ridingLocation = new RidingLocation(memberId,longitude,latitude,targetSpeed);
            ridingLocationRepository.save(ridingLocation);
        }

        if(packMode == true)
        {
            List<RidingLocation> dbResult =  ridingLocationRepository.findNearbyUsers(memberId,longitude,latitude,targetSpeed);
            if(dbResult.size() == 0)
            {
                return new ResponseDataDto<>("No Matching Data",204,null);
            }
            List<UserLocationResultDto> result = dbResult.stream().map(m->
                            new UserLocationResultDto(memberRepository.findById(m.getId()).get().getNickname(),m.getLongitude(),m.getLatitude()))
                    .collect(Collectors.toList());
            return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);
        }
        else
        {
            return new ResponseDataDto<>("OK", 200, null);
        }


//        List<RidingLocationWithDistance> dbResult =  ridingLocationRepository.findNearbyUsersWithDistance(memberId,longitude,latitude);
//        if(dbResult.size() == 0)
//        {
//            return new ResponseDataDto<>("No Matching Data",204,null);
//        }
//        List<UserLocationWithDistanceDto> result = dbResult.stream().map(m->
//                        new UserLocationWithDistanceDto(m.getId(),m.getLongitude(),m.getLatitude(),m.getDistance()))
//                .collect(Collectors.toList());
//        return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);
    }

    public ResponseNoDataDto checkDirtyMemoryRepository(String memberId)
    {
        if(ridingCoordinateRepository.findById(memberId).isPresent()) {
            ridingCoordinateRepository.deleteById(memberId);
            return new ResponseNoDataDto("detected abnormal termination, but now handled",200);
        }
        if(ridingSpeedRepository.findById(memberId).isPresent()) {
            ridingSpeedRepository.deleteById(memberId);
            return new ResponseNoDataDto("detected abnormal termination, but now handled",200);
        }

        return new ResponseNoDataDto("OK", 200);

    }
}
