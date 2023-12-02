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
    public ResponseDataDto getWholeRidingRecord(String memberId, Long bicycleId)
    {
        ResponseDataDto<ManagementRecordInfoDto> result = new ResponseDataDto<>();

        List<ManagementRecord> dbResult =  managementRecordRepository.findMatchingRecord(memberId,bicycleId);
        if(dbResult.size() == 0)
        {
            result.setMessage("No Matching Data");
            result.setStatus(204);
            result.setData(null);
            return result;
        }
        ManagementRecordInfoDto managementRecordInfoDto = new ManagementRecordInfoDto();
        List<ManagementRecordSimplifiedDto> listResult = dbResult.stream().map(m->
                        new ManagementRecordSimplifiedDto(m.getManagementTime(),
                                (m.getGears() == ManagementRecord.CHANGED ? 1:0) +
                                        (m.getFrontTire() == ManagementRecord.CHANGED? 1 : 0) +
                                        (m.getRearTire() == ManagementRecord.CHANGED? 1 : 0)+
                                        (m.getChain() == ManagementRecord.CHANGED ? 1 : 0) +
                                        (m.getBrakes() == ManagementRecord.CHANGED ? 1 : 0)
                                ,m.getId()))
                .collect(Collectors.toList());
        managementRecordInfoDto.setRecords(listResult);

        BicycleStatusDto bicycleStatusDto = new BicycleStatusDto();
        Optional<ManagementRecord> thisRecord;
        Optional<Bicycle> targetBicycle = bicycleRepository.findById(bicycleId);

        if(targetBicycle.isPresent()) {
            bicycleStatusDto.setBicycleName(targetBicycle.get().getBicycleName());
        }
        else {
            result.setMessage("No such Bicycle");
            result.setStatus(204);
            result.setData(null);
            return result;
        }
        //latestFrontTireChange
        thisRecord = managementRecordRepository.findTopByBicycleIdAndFrontTireOrderByManagementTimeDesc(bicycleId,ManagementRecord.CHANGED);
        if(thisRecord.isPresent()) {
            bicycleStatusDto.setFrontTireExchangeTime(thisRecord.get().getManagementTime());
            String lifeTime;
            List<RidingRecord> ridingRecords = ridingRecordRepository.findMatchingRecordAfter(bicycleId,thisRecord.get().getManagementTime());
            Double ridingDistance = 0d;
            for(RidingRecord ridingRecord : ridingRecords) {
                ridingDistance += ridingRecord.getDistance();
            }
            lifeTime = String.format("%.1f / %d (km)",ridingDistance,thisRecord.get().getFrontTireLife());
            bicycleStatusDto.setFrontTireLeftLife(lifeTime);

        }
        else {
            bicycleStatusDto.setFrontTireExchangeTime(0L);
            bicycleStatusDto.setFrontTireLeftLife("no information");
        }
        //latestRearTireChange
        thisRecord = managementRecordRepository.findTopByBicycleIdAndRearTireOrderByManagementTimeDesc(bicycleId,ManagementRecord.CHANGED);
        if(thisRecord.isPresent()) {
            bicycleStatusDto.setRearTireExchangeTime(thisRecord.get().getManagementTime());
            String lifeTime;
            List<RidingRecord> ridingRecords = ridingRecordRepository.findMatchingRecordAfter(bicycleId,thisRecord.get().getManagementTime());
            Double ridingDistance = 0d;
            for(RidingRecord ridingRecord : ridingRecords) {
                ridingDistance += ridingRecord.getDistance();
            }
            lifeTime = String.format("%.1f / %d (km)",ridingDistance,thisRecord.get().getRearTireLife());
            bicycleStatusDto.setRearTireLeftLife(lifeTime);
        }
        else {
            bicycleStatusDto.setFrontTireExchangeTime(0L);
            bicycleStatusDto.setFrontTireLeftLife("no information");
        }
        //gearExchange
        thisRecord = managementRecordRepository.findTopByBicycleIdAndGearsOrderByManagementTimeDesc(bicycleId,ManagementRecord.CHANGED);
        if(thisRecord.isPresent()) {
            bicycleStatusDto.setGearExchangeTime(thisRecord.get().getManagementTime());
        }
        else {
            bicycleStatusDto.setGearExchangeTime(0L);
        }
        //brakeExchange
        thisRecord = managementRecordRepository.findTopByBicycleIdAndBrakesOrderByManagementTimeDesc(bicycleId,ManagementRecord.CHANGED);
        if(thisRecord.isPresent()) {
            bicycleStatusDto.setBrakeExchangeTime(thisRecord.get().getManagementTime());
        }
        else {
            bicycleStatusDto.setBrakeExchangeTime(0L);
        }

        //Chain
        thisRecord = managementRecordRepository.findTopByBicycleIdAndChainOrderByManagementTimeDesc(bicycleId,ManagementRecord.CHANGED);
        if(thisRecord.isPresent()) {
            bicycleStatusDto.setChainExchangeTime(thisRecord.get().getManagementTime());
        }
        else {
            bicycleStatusDto.setChainExchangeTime(0L);
        }

        managementRecordInfoDto.setBicycleStatus(bicycleStatusDto);
        result.setData(managementRecordInfoDto);
        result.setMessage("OK");
        result.setStatus(200);
        return result;
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
        else if(!targetRecord.getBicycleId().equals(bicycleId))
        {
            return new ResponseDataDto<>("BicycleNo does not match with record ID",406,null);
        }
        return new ResponseDataDto<>("OK",200,dbResult.get());
    }

    @Transactional
    public ResponseNoDataDto postManagementRecord(ManagementRecordPostDto managementRecordPostDto,String memberId)
    {
        ManagementRecord entity = managementRecordPostDto.toEntity();
        entity.setMemberId(memberId);

        if(managementRecordPostDto.getFrontTire() != 2 && managementRecordPostDto.getFrontTireLife() != 0) {
            return new ResponseNoDataDto("You have TireLife when Tire hasn't been changed",403);
        }
        if(managementRecordPostDto.getRearTire() != 2 && managementRecordPostDto.getRearTireLife() != 0) {
            return new ResponseNoDataDto("You have TireLife when Tire hasn't been changed",403);
        }
        managementRecordRepository.save(entity);
        return new ResponseNoDataDto("OK",200);
    }

    public ResponseNoDataDto registerBicycle(BicycleRegisterRequestDto request,String memberId)
    {
        Optional<Member> optOwner =  memberRepository.findById(memberId);
        if(optOwner.isEmpty())
        {
            return new ResponseNoDataDto("there is no such user id",406);
        }
        Optional<Bicycle> result =  bicycleRepository.findBicycle(memberId, request.getBicycleName());
        if(result.isEmpty())
        {
            Member owner = optOwner.get();
            owner.setBicycleNumber(owner.getBicycleNumber()+1);
            memberRepository.save(owner);
            Bicycle entity = request.toEntity();
            entity.setOwnerId(memberId);
            bicycleRepository.save(entity);
            System.out.println(entity.getId());
            return new ResponseNoDataDto(""+entity.getId(),200);
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

    @Transactional
    public ResponseNoDataDto deleteManagementRecord(String memberId, Long recordId) {
        Optional<ManagementRecord> optionalManagementRecord = managementRecordRepository.findById(recordId);
        if(optionalManagementRecord.isEmpty()) {
            return new ResponseNoDataDto("no such entry with that recordId", 204);
        }
        else {
            ManagementRecord managementRecord = optionalManagementRecord.get();
            if(!managementRecord.getMemberId().equals(memberId)) {
                return new ResponseNoDataDto("trying to delete entry of other user",403);
            }
            else {
                managementRecordRepository.deleteById(recordId);
                return new ResponseNoDataDto("OK",200);
            }
        }
    }

    @Transactional
    public ResponseNoDataDto deleteBicycle(String memberId, Long bicycleId) {
        Optional<Bicycle> targetBicycle = bicycleRepository.findById(bicycleId);
        if(targetBicycle.isPresent()) {
            if(!targetBicycle.get().getOwnerId().equals(memberId))
            {
                return new ResponseNoDataDto("trying to delete other's bike", 403);

            }
            else {
                Optional<Member> member = memberRepository.findById(memberId);
                if(member.isPresent()) {
                    Member memberEntity = member.get();
                    memberEntity.setBicycleNumber(memberEntity.getBicycleNumber()-1);
                    memberRepository.save(memberEntity);
                }
                ridingRecordRepository.deleteAllByBicycleId(bicycleId);
                managementRecordRepository.deleteAllByBicycleId(bicycleId);
                bicycleRepository.deleteById(bicycleId);
                return new ResponseNoDataDto("OK", 200);
            }
        }
        else {
            return new ResponseNoDataDto("target Bike not found", 204);
        }
    }
}
