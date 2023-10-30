package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.RidingRecordPostDto;
import com.sba.recordingserver.dto.RidingRecordSimplifiedDto;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.service.RidingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RidingRecordController {
    @Autowired
    RidingRecordService ridingRecordService;

    @GetMapping(value = "/riding_record/whole_list")
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getWholeList(@RequestHeader(value="Authorization")String token, @RequestParam Integer bicycleNo)
    {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingRecordService.getWholeRidingRecord(memberId,bicycleNo);
    }

    @GetMapping(value = "/riding_record/list_after")
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getWholeListAfter(@RequestHeader(value="Authorization")String token, @RequestParam Integer bicycleNo, @RequestParam Long time) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingRecordService.getRidingRecordAfter(memberId,bicycleNo,time);
    }

    @GetMapping(value = "/riding_record/one")
    public ResponseDataDto<RidingRecord> getRecord(@RequestHeader(value="Authorization")String token, @RequestParam Integer bicycleNo, @RequestParam Long recordId) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return ridingRecordService.getRidingRecordDetail(memberId, bicycleNo, recordId);
    }

    @PostMapping(value= "/riding_record/post")
    public ResponseNoDataDto postRecord(@RequestBody RidingRecordPostDto postRequest) {
        return ridingRecordService.postRidingRecord(postRequest);
    }

}
