package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.ReportDto;
import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.repository.ReportRepository;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("/report/post")
    private ResponseNoDataDto reportUser(@RequestHeader(value="Authorization")String token, @RequestBody ReportDto reportDto) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return reportService.reportUser(memberId, reportDto);
    }

    @GetMapping("/report/get_all_reports")
    private ResponseDataDto getAllReports(@RequestHeader(value="Authorization")String token) {
        String managerId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return reportService.getAllReports(managerId);
    }

    @GetMapping("/report/get_my_reports")
    private ResponseDataDto getReportsWrittenByUser(@RequestHeader(value="Authorization")String token) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return reportService.getReportsWrittenByUser(memberId);
    }

    @GetMapping("/report/get_suspicious_users")
    private ResponseDataDto getTargetReportedMoreThanThreeTimes(@RequestHeader(value="Authorization")String token) {
        String managerId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return reportService.getTargetReportedMoreThanThreeTimes(managerId);
    }

    @PostMapping("/report/ban_user")
    private ResponseNoDataDto banUser(@RequestHeader(value="Authorization")String token, @RequestParam String memberId) {
        String managerId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return reportService.banUser(managerId,memberId);
    }

    @GetMapping("/report/get_all_user")
    private ResponseDataDto getAllUserInfo(@RequestHeader(value="Authorization")String token) {
        String managerId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return reportService.getAllUserInfo(managerId);
    }
}
