package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.RidingRecordSimplifiedDto;
import com.sba.recordingserver.dto.UserLocationDto;
import com.sba.recordingserver.dto.UserLocationWithDistanceDto;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.entity.RidingLocationWithDistance;
import com.sba.recordingserver.repository.RidingLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RidingLocationService {
    @Autowired
    RidingLocationRepository ridingLocationRepository;

    public ResponseDataDto<List<UserLocationDto>> saveLocationAndReturnNearbyUsers(String memberId, Double longitude, Double latitude)
    {

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
            RidingLocation ridingLocation = new RidingLocation(memberId,longitude,latitude);
            ridingLocationRepository.save(ridingLocation);
        }

        List<RidingLocation> dbResult =  ridingLocationRepository.findNearbyUsers(memberId,longitude,latitude);
        if(dbResult.size() == 0)
        {
            return new ResponseDataDto<>("No Matching Data",204,null);
        }
        List<UserLocationDto> result = dbResult.stream().map(m->
                        new UserLocationDto(m.getId(),m.getLongitude(),m.getLatitude()))
                .collect(Collectors.toList());
        return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);

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
}
