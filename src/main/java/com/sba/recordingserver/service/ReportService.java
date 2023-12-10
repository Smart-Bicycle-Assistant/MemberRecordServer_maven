package com.sba.recordingserver.service;


import com.sba.recordingserver.dto.ReportDto;
import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.entity.Manager;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.entity.Report;
import com.sba.recordingserver.repository.ManagerRepository;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    MemberRepository memberRepository;
    public ResponseNoDataDto reportUser(String memberId, ReportDto reportDto) {

        if(!memberId.equals(reportDto.getReporter())) {
            System.out.println("token owner doesn't match reportDto's reporter");
            return new ResponseNoDataDto("token owner doesn't match reportDto's reporter",403);
        }

        Optional<Member> optionalMember = memberRepository.findById(reportDto.getReporter());
        if(optionalMember.isEmpty()) {
            System.out.println("reporter id not found in db");
            return new ResponseNoDataDto("ghost member trying to report",206);
        }
        optionalMember = memberRepository.findById(reportDto.getTarget());
        if(optionalMember.isEmpty()) {
            System.out.println("target not found in db");
            return new ResponseNoDataDto("target not found in db",206);
        }
        reportRepository.save(reportDto.toEntity());
        return new ResponseNoDataDto("OK",200);
    }
    public ResponseDataDto getAllReports(String manager) {
        Optional<Manager> optionalManager = managerRepository.findById(manager);
        if(optionalManager.isEmpty()){
            System.out.println("Someone who's not manager trying to view reports");
            return new ResponseDataDto("you are not manager",403,null);
        }
        List<Report> reports = reportRepository.findAllByOrderByTimeDesc();
        ResponseDataDto<List<Report>> result = new ResponseDataDto<>("OK",200,reports);
        return result;
    }
    public ResponseDataDto getReportsWrittenByUser(String memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) {
            System.out.println("memberId not in DB");
            return new ResponseDataDto("memberId not in DB",206,null);
        }
        List<Report> reports = reportRepository.findAllByReporter(memberId);
        ResponseDataDto<List<Report>> result = new ResponseDataDto<>("OK",200,reports);
        return result;
    }
    public ResponseDataDto getTargetReportedMoreThanThreeTimes(String manager) {
        Optional<Manager> optionalManager = managerRepository.findById(manager);
        if(optionalManager.isEmpty()) {
            System.out.println("Someone who's not manager trying to view reports");
            return new ResponseDataDto("you are not manager",403,null);
        }
        List<Report> reports = reportRepository.findAllThoseReportedMoreThanThreeTimes();
        return new ResponseDataDto("OK",200,reports);
    }

    public ResponseNoDataDto banUser(String manager, String memberId) {
        Optional<Manager> optionalManager = managerRepository.findById(manager);
        if(optionalManager.isEmpty()) {
            System.out.println("Someone who's not manager trying to view reports");
            return new ResponseNoDataDto("you are not manager",403);
        }
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) {
            System.out.println("memberId not in DB");
            return new ResponseNoDataDto("memberId not in DB",206);
        }
        Member member = optionalMember.get();
        Long currentTime = System.currentTimeMillis();
        Long bannedUntil = 0L;
        if(member.getBanned() == -1) {
            bannedUntil = currentTime + 30000L;
//            bannedUntil = currentTime + 604800000L; //week
        }
        else {
            bannedUntil = currentTime + 30000L;
//            bannedUntil = member.getBanned() + 604800000L;
        }
        member.setBanned(bannedUntil);
        memberRepository.save(member);
        return new ResponseNoDataDto(member.getId() + " is banned until + "+bannedUntil,200);
    }
    public ResponseDataDto getAllUserInfo(String manager) {
        Optional<Manager> optionalManager = managerRepository.findById(manager);
        if(optionalManager.isEmpty()) {
            System.out.println("Someone who's not manager trying to view reports");
            return new ResponseDataDto("you are not manager",403,null);
        }
        List<Member> allUsers = memberRepository.findAll();
        return new ResponseDataDto("OK",200,allUsers);
    }
    public ResponseNoDataDto setSolvedStatus(String manager, Integer status, Long reportId) {
        Optional<Manager> optionalManager = managerRepository.findById(manager);
        if(optionalManager.isEmpty()) {
            System.out.println("Someone who's not manager trying to view reports");
            return new ResponseNoDataDto("you are not manager",403);
        }
        Optional<Report> optionalReport = reportRepository.findById(reportId) ;
        if(optionalReport.isEmpty()) {
            System.out.println("no such report");
            return new ResponseNoDataDto("no such report with that reportId",206);
        }
        Report report = optionalReport.get();
        report.setSolved(status);
        reportRepository.save(report);
        return new ResponseNoDataDto("OK",200);
    }
}
