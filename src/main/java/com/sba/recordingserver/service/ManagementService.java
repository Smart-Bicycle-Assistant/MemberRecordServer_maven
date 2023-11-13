package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.Bicycle;
import com.sba.recordingserver.entity.ManagementRecord;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.repository.BicycleRepository;
import com.sba.recordingserver.repository.ManagementRecordRepository;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.repository.RidingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagementService {
    @Autowired
    ManagementRecordRepository managementRecordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BicycleRepository bicycleRepository;

    @Autowired
    private RidingRecordRepository ridingRecordRepository;

    @Transactional
    public ResponseDataDto<List<ManagementRecordSimplifiedDto>> getWholeRidingRecord(String memberId, Long bicycleId)
    {
        List<ManagementRecord> dbResult =  managementRecordRepository.findMatchingRecord(memberId,bicycleId);
        if(dbResult.size() == 0)
        {
            return new ResponseDataDto<>("No Matching Data",204,null);
        }
        List<ManagementRecordSimplifiedDto> result = dbResult.stream().map(m->
                        new ManagementRecordSimplifiedDto(m.getManagementTime(),
                                (m.getGears() == ManagementRecord.CHANGED ? 1:0) +
                                        (m.getTire() == ManagementRecord.CHANGED ? 1 : 0) +
                                        (m.getChain() == ManagementRecord.CHANGED ? 1 : 0) +
                                        (m.getBrakes() == ManagementRecord.CHANGED ? 1 : 0)
                                ,m.getId()))
                .collect(Collectors.toList());
        return new ResponseDataDto<>("OK : Result Count = "+result.size(),200,result);
    }

    @Transactional
    public ResponseDataDto<ManagementRecord> getManagementRecordDetail(String memberId,Long bicycleId,Long Id)
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
        else if(targetRecord.getBicycleId() != bicycleId)
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

    public ResponseNoDataDto registerBicycle(BicycleRegisterRequestDto request)
    {
        Optional<Member> optOwner =  memberRepository.findById(request.getOwnerId());
        if(optOwner.isEmpty())
        {
            return new ResponseNoDataDto("there is no such user id",406);
        }
        Optional<Bicycle> result =  bicycleRepository.findBicycle(request.getOwnerId(), request.getBicycleName());
        if(result.isEmpty())
        {
            Member owner = optOwner.get();
            owner.setBicycleNumber(owner.getBicycleNumber()+1);
            memberRepository.save(owner);
            bicycleRepository.save(request.toEntity());
            return new ResponseNoDataDto("Register Success",200);
        }
        else
        {
            return new ResponseNoDataDto("owner has bicycle with same name",406);
        }
    }
    public ResponseDataDto getBicycleList(String memberId) {
        List<Bicycle> list = bicycleRepository.findAllByOwnerIdOrderById(memberId);
        System.out.println("out");
        List<BicycleInfoDto> targetList = new ArrayList<>();
        for(Bicycle thisBicycle : list) {
            System.out.println("before");
            Double distance = ridingRecordRepository.findSumOfTargetBicycle(thisBicycle.getId());
            System.out.println("after");
            targetList.add(new BicycleInfoDto(thisBicycle.getId(), thisBicycle.getBicycleName(), thisBicycle.getBicycleImage(), thisBicycle.getRegisterTime(), distance));
        }
        return new ResponseDataDto("OK",200,targetList);
    }
}
