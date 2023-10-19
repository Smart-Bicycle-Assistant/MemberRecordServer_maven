package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.UserLocationResultDto;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.repository.RidingCoordinateMemoryRepository;
import com.sba.recordingserver.repository.RidingLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RidingLocationService {
    @Autowired
    RidingLocationRepository ridingLocationRepository;
    @Autowired
    MemberRepository memberRepository;
    RidingCoordinateMemoryRepository ridingCoordinateMemoryRepository = RidingCoordinateMemoryRepository.getInstance();
    public ResponseDataDto<List<UserLocationResultDto>> saveLocationAndReturnNearbyUsers(String memberId, Double longitude, Double latitude, Boolean packMode, Double speed)
    {
        ridingCoordinateMemoryRepository.save(memberId,longitude,latitude);

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
            RidingLocation ridingLocation = new RidingLocation(memberId,longitude,latitude,speed);
            ridingLocationRepository.save(ridingLocation);
        }

        if(packMode == true)
        {
            List<RidingLocation> dbResult =  ridingLocationRepository.findNearbyUsers(memberId,longitude,latitude,speed);
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
        String result = ridingCoordinateMemoryRepository.findById(memberId);
        if(result != null) {
            ridingCoordinateMemoryRepository.remove(memberId);
            return new ResponseNoDataDto("detected termination, but now handled",200);
        }
        else
        {
            return new ResponseNoDataDto("OK",200);
        }
    }
}
