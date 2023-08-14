package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.RidingLogPostDto;
import com.sba.recordingserver.dto.RidingRecordSimplifiedDto;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.service.RidingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RidingRecordController {
    @Autowired
    RidingRecordService ridingRecordService;

    @GetMapping(value = "/riding_record/whole_list")
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getWholeList(@RequestParam String memberId, @RequestParam Integer bicycleNo)
    {
        return ridingRecordService.getWholeRidingRecord(memberId,bicycleNo);
    }

    @GetMapping(value = "/riding_record/list_after")
    public ResponseDataDto<List<RidingRecordSimplifiedDto>> getWholeListAfter(@RequestParam String memberId, @RequestParam Integer bicycleNo, @RequestParam Long time) {
        return ridingRecordService.getRidingRecordAfter(memberId,bicycleNo,time);
    }

    @GetMapping(value = "/riding_record/one")
    public ResponseDataDto<RidingRecord> getRecord(@RequestParam String memberId, @RequestParam Integer bicycleNo, @RequestParam Long recordId) {
        return ridingRecordService.getRidingRecordDetail(memberId, bicycleNo, recordId);
    }

    @PostMapping(value= "/riding_record/post")
    public ResponseNoDataDto postRecord(@RequestBody RidingLogPostDto postRequest) {
        return ridingRecordService.postRidingLog(postRequest);
    }

}
