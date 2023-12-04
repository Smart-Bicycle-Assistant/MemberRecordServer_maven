package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.RidingRecordPostDto;
import com.sba.recordingserver.dto.RidingRecordSimplifiedDto;
import com.sba.recordingserver.entity.RidingCoordinate;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.entity.RidingRecord;
import com.sba.recordingserver.entity.RidingSpeed;
import com.sba.recordingserver.repository.RidingCoordinateRepository;
import com.sba.recordingserver.repository.RidingLocationRepository;
import com.sba.recordingserver.repository.RidingRecordRepository;
import com.sba.recordingserver.repository.RidingSpeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RidingRecordServiceTest {

    @Mock
    private RidingRecordRepository ridingRecordRepository;

    @Mock
    private RidingCoordinateRepository ridingCoordinateRepository;

    @Mock
    private RidingLocationRepository ridingLocationRepository;

    @Mock
    private RidingSpeedRepository ridingSpeedRepository;

    @InjectMocks
    private RidingRecordService ridingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetWholeRidingRecord() {
        // Mock the repository response
        List<RidingRecord> mockRidingRecords = new ArrayList<>();
        mockRidingRecords.add(new RidingRecord(/* initialize with necessary data */));
        when(ridingRecordRepository.findMatchingRecord(anyLong())).thenReturn(mockRidingRecords);

        // Call the method
        ResponseDataDto<List<RidingRecordSimplifiedDto>> result = ridingRecordService.getWholeRidingRecord("testMemberId", 123L);

        // Assertions
        assertEquals("OK : Result Count = 1", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testGetRidingRecordAfter() {
        // Mock the repository response
        List<RidingRecord> mockRidingRecords = new ArrayList<>();
        mockRidingRecords.add(new RidingRecord(/* initialize with necessary data */));
        when(ridingRecordRepository.findMatchingRecordAfter(anyLong(), anyLong())).thenReturn(mockRidingRecords);

        // Call the method
        ResponseDataDto<List<RidingRecordSimplifiedDto>> result = ridingRecordService.getRidingRecordAfter("testMemberId", 123L, 456L);

        // Assertions
        assertEquals("OK : Result Count = 1", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testGetRidingRecordDetail() {
        // Mock the repository response
        RidingRecord mockRidingRecord = new RidingRecord();
        mockRidingRecord.setMemberId("testMemberId");
        mockRidingRecord.setBicycleId(123L);
        mockRidingRecord.setId(456L);
        Optional<RidingRecord> optionalRidingRecord = Optional.of(mockRidingRecord);

        when(ridingRecordRepository.findById(anyLong())).thenReturn(optionalRidingRecord);

        // Call the method
        ResponseDataDto<RidingRecord> result = ridingRecordService.getRidingRecordDetail("testMemberId", 123L, 456L);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(mockRidingRecord, result.getData());
    }

    @Test
    void testDeleteRidingRecord() {
        // Mock the repository response
        RidingRecord mockRidingRecord = new RidingRecord(/* initialize with necessary data */);
        mockRidingRecord.setMemberId("testMemberId");
        Optional<RidingRecord> optionalRidingRecord = Optional.of(mockRidingRecord);
        when(ridingRecordRepository.findById(anyLong())).thenReturn(optionalRidingRecord);

        // Call the method
        ResponseNoDataDto result = ridingRecordService.deleteRidingRecord("testMemberId", 456L);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }

    @Test
    void testPostRidingRecord() {
        // Mock the repository responses
        RidingCoordinate ridingCoordinate = new RidingCoordinate();
        ridingCoordinate.setLongitude("[123.1]");
        ridingCoordinate.setLatitude("[37.1]");
        ridingCoordinate.setId("member");
        RidingSpeed ridingSpeed = new RidingSpeed();
        ridingSpeed.setSpeed("[123]");

        RidingLocation ridingLocation = new RidingLocation();
        when(ridingCoordinateRepository.findById(anyString())).thenReturn(Optional.of(ridingCoordinate));
        when(ridingSpeedRepository.findById(anyString())).thenReturn(Optional.of(ridingSpeed));
        when(ridingLocationRepository.findById(anyString())).thenReturn(Optional.of(ridingLocation));

        // Create a mock RidingRecordPostDto
        RidingRecordPostDto postDto = new RidingRecordPostDto(/* initialize with necessary data */);
        postDto.setRidingTime(1L);
        postDto.setRidingDuration(1L);
        postDto.setDistance(12d);
        postDto.setMaxSpeed(12d);
        postDto.setAvgSpeed(12d);


        // Call the method
        ResponseNoDataDto result = ridingRecordService.postRidingRecord(postDto, "testMemberId");

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }
}
