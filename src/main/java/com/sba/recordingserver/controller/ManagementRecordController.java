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
    ResponseDataDto<List<ManagementRecordSimplifiedDto>> getWholeList(@RequestHeader(value="Authorization")String token, @RequestParam Integer bicycleNo) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return managementService.getWholeRidingRecord(memberId, bicycleNo);
    }

    @GetMapping("/management_record/one")
    public ResponseDataDto<ManagementRecord> getRecord(@RequestHeader(value="Authorization")String token, @RequestParam Integer bicycleNo, @RequestParam Long recordId) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return managementService.getManagementRecordDetail(memberId, bicycleNo, recordId);
    }

    @PostMapping("management_record/post")
    public ResponseNoDataDto postRecord(@RequestBody ManagementRecordPostDto postRequest) {
        return managementService.postManagementRecord(postRequest);
    }
}
