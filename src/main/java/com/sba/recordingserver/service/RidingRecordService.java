package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.RidingLogPostDto;
import com.sba.recordingserver.dto.RidingRecordSimplifiedDto;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.repository.RidingRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class RidingRecordService {

    @Autowired
    RidingRecordRepository ridingRecordRepository;

    @Transactional
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getWholeRidingRecord(String memberId, Integer bicycleNo)
    {
        List<RidingRecord> dbResult =  ridingRecordRepository.findMatchingRecord(memberId,bicycleNo);
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
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getRidingRecordAfter(String memberId, Integer bicycleNo, Long time)
    {
        List<RidingRecord> dbResult = ridingRecordRepository.findMatchingRecordAfter(memberId, bicycleNo, time);
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
    public ResponseDataDto<RidingRecord> getRidingRecordDetail(String memberId,Integer bicycleNo,Long Id)
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
        else if(targetRecord.getBicycleNo() != bicycleNo)
        {
            return new ResponseDataDto<>("BicycleNo does not match with record ID",406,null);
        }
        return new ResponseDataDto<>("OK",200,dbResult.get());
    }

    @Transactional
    public ResponseNoDataDto postRidingLog(RidingLogPostDto postRequest)
    {
        ridingRecordRepository.save(postRequest.toEntity());
        return new ResponseNoDataDto("OK",200);
    }
}
