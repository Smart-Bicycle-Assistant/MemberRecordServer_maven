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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ManagementServiceTest {
    @Mock
    private ManagementRecordRepository managementRecordRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BicycleRepository bicycleRepository;

    @Mock
    private RidingRecordRepository ridingRecordRepository;

    @InjectMocks
    private ManagementService managementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetWholeRidingRecord() {
        // Mock example data
        String memberId = "testMemberId";
        Long bicycleId = 1L;
        Long managementRecordId = 1L;

        ManagementRecord managementRecord = new ManagementRecord();
        managementRecord.setId(managementRecordId);
        managementRecord.setManagementTime(System.currentTimeMillis());
        managementRecord.setGears(ManagementRecord.CHANGED);
        managementRecord.setFrontTire(ManagementRecord.CHANGED);
        managementRecord.setRearTire(ManagementRecord.CHANGED);
        managementRecord.setChain(ManagementRecord.CHANGED);
        managementRecord.setBrakes(ManagementRecord.CHANGED);

        Bicycle bicycle = new Bicycle();
        bicycle.setId(bicycleId);
        bicycle.setBicycleName("TestBike");

        when(managementRecordRepository.findMatchingRecord(anyString(), anyLong())).thenReturn(List.of(managementRecord));
        when(bicycleRepository.findById(anyLong())).thenReturn(Optional.of(bicycle));
        when(ridingRecordRepository.findMatchingRecordAfter(anyLong(), anyLong())).thenReturn(List.of(new RidingRecord()));

        // Call the method
        ResponseDataDto<ManagementRecordInfoDto> result = managementService.getWholeRidingRecord(memberId, bicycleId);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());

        ManagementRecordInfoDto managementRecordInfoDto = result.getData();
        assertNotNull(managementRecordInfoDto.getRecords());
        assertEquals(1, managementRecordInfoDto.getRecords().size());

        ManagementRecordSimplifiedDto simplifiedDto = managementRecordInfoDto.getRecords().get(0);
        assertEquals(managementRecordId, simplifiedDto.getRecordId());
        assertEquals(5, simplifiedDto.getNumFixed()); // Assuming 5 components were changed in the mock data
    }

    @Test
    void testGetManagementRecordDetail() {
        // Mock example data
        String memberId = "testMemberId";
        Long bicycleId = 1L;
        Long recordId = 1L;

        ManagementRecord managementRecord = new ManagementRecord();
        managementRecord.setId(recordId);
        managementRecord.setMemberId(memberId);
        managementRecord.setBicycleId(bicycleId);

        when(managementRecordRepository.findById(anyLong())).thenReturn(Optional.of(managementRecord));

        // Call the method
        ResponseDataDto<ManagementRecord> result = managementService.getManagementRecordDetail(memberId, bicycleId, recordId);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());

        ManagementRecord returnedRecord = result.getData();
        assertEquals(recordId, returnedRecord.getId());
        assertEquals(memberId, returnedRecord.getMemberId());
        assertEquals(bicycleId, returnedRecord.getBicycleId());
    }

    @Test
    void testPostManagementRecord() {
        // Mock example data
        String memberId = "testMemberId";
        Long recordId = 1L;

        ManagementRecordPostDto postDto = new ManagementRecordPostDto();
        postDto.setGears(ManagementRecord.CHANGED);
        postDto.setFrontTire(ManagementRecord.CHANGED);
        postDto.setRearTire(ManagementRecord.CHANGED);
        postDto.setChain(ManagementRecord.CHANGED);
        postDto.setBrakes(ManagementRecord.CHANGED);

        when(managementRecordRepository.save(any(ManagementRecord.class))).thenReturn(new ManagementRecord());

        // Call the method
        ResponseNoDataDto result = managementService.postManagementRecord(postDto, memberId);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }

    @Test
    void testRegisterBicycle() {
        // Mock example data
        String memberId = "testMemberId";
        BicycleRegisterRequestDto registerRequestDto = new BicycleRegisterRequestDto();
        registerRequestDto.setBicycleName("TestBike");

        Member member = new Member();
        member.setId(memberId);
        member.setBicycleNumber(0);

        when(memberRepository.findById(anyString())).thenReturn(Optional.of(member));
        when(bicycleRepository.findBicycle(anyString(), anyString())).thenReturn(Optional.empty());

        // Simulate saving the Bicycle entity
        when(bicycleRepository.save(any(Bicycle.class))).thenAnswer(invocation -> {
            Bicycle savedBicycle = invocation.getArgument(0);
            savedBicycle.setId(1L); // Set the ID manually or use a builder method if available
            return savedBicycle;
        });

        // Call the method
        ResponseNoDataDto result = managementService.registerBicycle(registerRequestDto, memberId);

        // Assertions
        assertEquals("1", result.getMessage()); // Adjust the expected ID based on your scenario
        assertEquals(200, result.getStatus());
    }

    @Test
    void testGetBicycleList() {
        // Mock example data
        String memberId = "testMemberId";
        List<Bicycle> bicycleList = new ArrayList<>();

        Bicycle bike1 = new Bicycle();
        bike1.setId(1L);
        bike1.setBicycleName("Bike1");
        // Set other properties if needed
        bicycleList.add(bike1);

        Bicycle bike2 = new Bicycle();
        bike2.setId(2L);
        bike2.setBicycleName("Bike2");
        // Set other properties if needed
        bicycleList.add(bike2);

        when(bicycleRepository.findAllByOwnerIdOrderById(anyString())).thenReturn(bicycleList);
        when(ridingRecordRepository.findSumOfTargetBicycle(anyLong())).thenReturn(100.0); // Adjust the distance based on your scenario

        // Call the method
        ResponseDataDto<List<BicycleInfoDto>> result = managementService.getBicycleList(memberId);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());

        List<BicycleInfoDto> bicycleInfoList = result.getData();
        assertEquals(2, bicycleInfoList.size());

        // Check the first bicycle info
        BicycleInfoDto firstBicycleInfo = bicycleInfoList.get(0);
        assertEquals(1L, firstBicycleInfo.getBicycleId());
        assertEquals("Bike1", firstBicycleInfo.getBicycleName());
        assertEquals(100.0, firstBicycleInfo.getDistance());

        // Check the second bicycle info
        BicycleInfoDto secondBicycleInfo = bicycleInfoList.get(1);
        assertEquals(2L, secondBicycleInfo.getBicycleId());
        assertEquals("Bike2", secondBicycleInfo.getBicycleName());
        // Add assertions for other properties as needed
    }
    @Test
    void testDeleteManagementRecord() {
        // Mock example data
        String memberId = "testMemberId";
        Long recordId = 1L;

        ManagementRecord managementRecord = new ManagementRecord();
        managementRecord.setId(recordId);
        managementRecord.setMemberId(memberId);

        when(managementRecordRepository.findById(anyLong())).thenReturn(Optional.of(managementRecord));

        // Call the method
        ResponseNoDataDto result = managementService.deleteManagementRecord(memberId, recordId);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }

    @Test
    void testDeleteBicycle() {
        // Mock example data
        String memberId = "testMemberId";
        Long bicycleId = 1L;

        // Mock the Member
        Member member = new Member();
        member.setId(memberId);
        member.setBicycleNumber(1);

        // Mock the Bicycle
        Bicycle bicycle = new Bicycle();
        bicycle.setId(bicycleId);
        bicycle.setOwnerId(memberId);
        bicycle.setBicycleImage("hey");
        bicycle.setRegisterTime(1L);


        // Mocking findById methods
        when(bicycleRepository.findById(anyLong())).thenReturn(Optional.of(bicycle));
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(member));

        // Mocking any other relevant methods

        // Call the method
//        System.out.println(managementService);
        ResponseNoDataDto result = managementService.deleteBicycle(memberId, bicycleId);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }
}
