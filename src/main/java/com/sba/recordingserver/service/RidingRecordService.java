package com.sba.recordingserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.RidingRecordPostDto;
import com.sba.recordingserver.dto.RidingRecordSimplifiedDto;
import com.sba.recordingserver.entity.RidingCoordinate;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.entity.RidingSpeed;
import com.sba.recordingserver.repository.*;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RidingRecordService {


    @Autowired
    RidingLocationRepository ridingLocationRepository;
    @Autowired
    RidingRecordRepository ridingRecordRepository;

    @Autowired
    RidingSpeedRepository ridingSpeedRepository;
    @Autowired
    RidingCoordinateRepository ridingCoordinateRepository;

//    RidingCoordinateMemoryRepository ridingCoordinateMemoryRepository = RidingCoordinateMemoryRepository.getInstance();
//    RidingSpeedMemoryRepository ridingSpeedMemoryRepository = RidingSpeedMemoryRepository.getInstance();

    @Transactional
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getWholeRidingRecord(String memberId, Long bicycleId)
    {
        List<RidingRecord> dbResult =  ridingRecordRepository.findMatchingRecord(bicycleId);
        if(dbResult.size() == 0)
        {
            return new ResponseDataDto<>("No Matching Data",204,null);
        }
        List<RidingRecordSimplifiedDto> result = dbResult.stream().map(m->
                new RidingRecordSimplifiedDto(m.getRidingTime(), m.getDistance(), m.getAvgSpeed(), m.getRidingDuration(),m.getId()))
                .collect(Collectors.toList());
        return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);
    }

    @Transactional
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getRidingRecordAfter(String memberId, Long bicycleId, Long time)
    {
        List<RidingRecord> dbResult = ridingRecordRepository.findMatchingRecordAfter(bicycleId, time);
        if(dbResult.size() == 0)
        {
            return new ResponseDataDto<>("No Matching Data",204,null);
        }
        List<RidingRecordSimplifiedDto> result = dbResult.stream().map(m->
                new RidingRecordSimplifiedDto(m.getRidingTime(),m.getDistance(),m.getAvgSpeed(),m.getRidingDuration(),m.getId()))
                .collect(Collectors.toList());
        return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);
    }

    @Transactional
    public ResponseDataDto<RidingRecord> getRidingRecordDetail(String memberId,Long bicycleNo,Long Id)
    {
        Optional<RidingRecord> dbResult = ridingRecordRepository.findById(Id);
        if(dbResult.isEmpty())
        {
            return new ResponseDataDto<>("No Matching Result",204,null);
        }

        RidingRecord targetRecord = dbResult.get();
        if(!targetRecord.getMemberId().equals(memberId))
        {
            return new ResponseDataDto<>("Trying to get record of other member",403,null);
        }
        else if(targetRecord.getBicycleId() != bicycleNo)
        {
            return new ResponseDataDto<>("BicycleNo does not match with record ID",406,null);
        }
        return new ResponseDataDto<>("OK",200,dbResult.get());
    }

    @Transactional
    public ResponseNoDataDto postRidingRecord(RidingRecordPostDto postRequest,String memberId)
    {
//        System.out.println(ridingCoordinateMemoryRepository.findById(postRequest.getMemberId()));
//        String map = ridingCoordinateMemoryRepository.findById(memberId);
//        String listSpeed = ridingSpeedMemoryRepository.findById(memberId);

        String map = null;
        String speed;
        Optional<RidingCoordinate> ridingCoordinate = ridingCoordinateRepository.findById(memberId);
        Optional<RidingSpeed> ridingSpeed = ridingSpeedRepository.findById(memberId);

        if(ridingCoordinate.isEmpty()) {
            map = "no data";
        }
        if(ridingSpeed.isEmpty()) {
            speed = "no data";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Double> listLatitude = objectMapper.readValue(ridingCoordinate.get().getLatitude(), new TypeReference<List<Double>>() {
            });
            List<Double> listLongitude = objectMapper.readValue(ridingCoordinate.get().getLongitude(), new TypeReference<List<Double>>() {
            });
            if(listLatitude.size() != listLongitude.size()) {
                return new ResponseNoDataDto("error in coordinate data",204);
            }

            List<Coordinate> coordinates = new ArrayList<>();
            for(int i =0; i< listLatitude.size(); i++) {
                coordinates.add(new Coordinate(listLongitude.get(i),listLatitude.get(i)));
            }
            map = new Gson().toJson(coordinates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        speed = ridingSpeed.get().getSpeed();
        RidingRecord entity = postRequest.toEntity("");
        entity.setMap(map);
        entity.setListSpeed(ridingSpeed.get().getSpeed());
        entity.setMemberId(memberId);
        ridingRecordRepository.save(entity);
        ridingCoordinateRepository.deleteById(memberId);
        ridingSpeedRepository.deleteById(memberId);
//        ridingCoordinateMemoryRepository.remove(memberId);
//        ridingSpeedMemoryRepository.remove(memberId);
        if(ridingLocationRepository.findById(memberId).isPresent())
        {
            ridingLocationRepository.deleteById(memberId);
        }
        return new ResponseNoDataDto("OK",200);
    }
}
class Coordinate {
    Double longitude;
    Double latitude;
    public Coordinate(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}