package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.ManagementRecord;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagementRecordController {

    @Autowired
    ManagementService managementService;

    @GetMapping("/management_record/whole_list")
    ResponseDataDto<List<ManagementRecordSimplifiedDto>> getWholeList(@RequestHeader(value="Authorization")String token, @RequestParam Long bicycleId) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return managementService.getWholeRidingRecord(memberId, bicycleId);
    }

    @GetMapping("/management_record/one")
    public ResponseDataDto<ManagementRecord> getRecord(@RequestHeader(value="Authorization")String token, @RequestParam Long bicycleId, @RequestParam Long recordId) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return managementService.getManagementRecordDetail(memberId, bicycleId, recordId);
    }

    @PostMapping("management_record/post")
    public ResponseNoDataDto postRecord(@RequestBody ManagementRecordPostDto postRequest) {
        return managementService.postManagementRecord(postRequest);
    }


    @PostMapping(value="management_record/register_bicycle")
    public ResponseNoDataDto registerBicycle(@RequestBody BicycleRegisterRequestDto request)
    {
        return managementService.registerBicycle(request);
    }
    @GetMapping(value="management_record/get_bicycle_list")
    public ResponseDataDto getBicycleList(@RequestHeader(value="Authorization")String token) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return managementService.getBicycleList(memberId);
    }

    @DeleteMapping(value="delete_bicycle")
    public ResponseNoDataDto deleteBicycle(@RequestHeader(value="Authorization")String token, @RequestParam Long bicycleId) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return managementService.deleteBicycle(memberId, bicycleId);
    }
}
