package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.ManagementRecord;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.repository.BicycleRepository;
import com.sba.recordingserver.repository.ManagementRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagementService {
    @Autowired
    ManagementRecordRepository managementRecordRepository;

    @Transactional
    public ResponseDataDto<List<ManagementRecordSimplifiedDto>> getWholeRidingRecord(String memberId, Integer bicycleNo)
    {
        List<ManagementRecord> dbResult =  managementRecordRepository.findMatchingRecord(memberId,bicycleNo);
        if(dbResult.size() == 0)
        {
            return new ResponseDataDto<>("No Matching Data",204,null);
        }
        List<ManagementRecordSimplifiedDto> result = dbResult.stream().map(m->
                        new ManagementRecordSimplifiedDto(m.getManagementTime(),
                                (m.getGears() == ManagementRecord.CHANGED ? 1:0) +
                                        (m.getTyre() == ManagementRecord.CHANGED ? 1 : 0) +
                                        (m.getChain() == ManagementRecord.CHANGED ? 1 : 0) +
                                        (m.getBrakes() == ManagementRecord.CHANGED ? 1 : 0)
                                ,m.getId()))
                .collect(Collectors.toList());
        return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);
    }

    @Transactional
    public ResponseDataDto<ManagementRecord> getManagementRecordDetail(String memberId,Integer bicycleNo,Long Id)
    {
        Optional<ManagementRecord> dbResult = managementRecordRepository.findById(Id);
        if(dbResult.isEmpty())
        {
            return new ResponseDataDto<>("No Matching Result",204,null);
        }

        ManagementRecord targetRecord = dbResult.get();
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
    public ResponseNoDataDto postManagementRecord(ManagementRecordPostDto managementRecordPostDto)
    {
        managementRecordRepository.save(managementRecordPostDto.toEntity());
        return new ResponseNoDataDto("OK",200);
    }
}
