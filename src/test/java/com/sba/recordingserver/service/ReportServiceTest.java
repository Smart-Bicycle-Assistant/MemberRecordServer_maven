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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReportServiceTest {
    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testReportUser() {
        // Mock the repository response
        Member member = new Member();
        member.setBanned(-1L);
        member.setId("testMemberId");
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(reportRepository.save(any())).thenReturn(new Report());

        // Create a mock ReportDto
        ReportDto reportDto = new ReportDto(/* initialize with necessary data */);
        reportDto.setReporter("testMemberId");

        // Call the method
        ResponseNoDataDto result = reportService.reportUser("testMemberId", reportDto);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }

    @Test
    void testGetAllReports() {
        // Mock the repository response
        when(managerRepository.findById(any())).thenReturn(Optional.of(new Manager()));
        when(reportRepository.findAllByOrderByTimeDesc()).thenReturn(new ArrayList<>());

        // Call the method
        ResponseDataDto<List<Report>> result = reportService.getAllReports("testManagerId");

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(new ArrayList<>(), result.getData());
    }

    @Test
    void testGetReportsWrittenByUser() {
        // Mock the repository response
        when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
        when(reportRepository.findAllByReporter(any())).thenReturn(new ArrayList<>());

        // Call the method
        ResponseDataDto<List<Report>> result = reportService.getReportsWrittenByUser("testMemberId");

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(new ArrayList<>(), result.getData());
    }

    @Test
    void testGetTargetReportedMoreThanThreeTimes() {
        // Mock the repository response
        when(managerRepository.findById(any())).thenReturn(Optional.of(new Manager()));
        when(reportRepository.findAllThoseReportedMoreThanThreeTimes()).thenReturn(new ArrayList<>());

        // Call the method
        ResponseDataDto<List<Report>> result = reportService.getTargetReportedMoreThanThreeTimes("testManagerId");

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(new ArrayList<>(), result.getData());
    }

    @Test
    void testBanUser() {
        // Mock the repository response
        when(managerRepository.findById(any())).thenReturn(Optional.of(new Manager()));
        Member member = new Member();
        member.setBanned(-1L);
        member.setId("testManagerId");
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(memberRepository.save(any())).thenReturn(new Member());

        // Call the method
        ResponseNoDataDto result = reportService.banUser("testManagerId", "testMemberId");

        // Assertions
        assertEquals(200, result.getStatus());
    }
}
