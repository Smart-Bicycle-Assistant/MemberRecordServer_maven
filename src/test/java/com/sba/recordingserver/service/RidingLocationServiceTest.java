package com.sba.recordingserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sba.recordingserver.dto.ResponseDataDto;
import com.sba.recordingserver.dto.ResponseNoDataDto;
import com.sba.recordingserver.dto.UserLocationResultDto;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.entity.RidingCoordinate;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.entity.RidingSpeed;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.repository.RidingCoordinateRepository;
import com.sba.recordingserver.repository.RidingLocationRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RidingLocationServiceTest {

    @Mock
    private RidingCoordinateRepository ridingCoordinateRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RidingSpeedRepository ridingSpeedRepository;

    @Mock
    private RidingLocationRepository ridingLocationRepository;

    @InjectMocks
    private RidingLocationService ridingLocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveLocationAndReturnNearbyUsers() {
        // Mock example data
        String memberId = "testMemberId";
        Double longitude = 10.0;
        Double latitude = 20.0;
        Boolean packMode = true;
        Double targetSpeed = 30.0;
        Double curSpeed = 25.0;

        ArrayList<RidingLocation> nearbyUsers = new ArrayList<>();
        nearbyUsers.add(new RidingLocation("test",1d,1d,1d));

        // Mock RidingCoordinate
        when(ridingCoordinateRepository.findById(anyString())).thenReturn(Optional.empty());
        when(ridingCoordinateRepository.save(any(RidingCoordinate.class))).thenReturn(new RidingCoordinate());


        // Mock RidingSpeed
        when(ridingSpeedRepository.findById(anyString())).thenReturn(Optional.empty());
        when(ridingSpeedRepository.save(any(RidingSpeed.class))).thenReturn(new RidingSpeed());

        // Mock RidingLocation
        when(ridingLocationRepository.findById(anyString())).thenReturn(Optional.empty());
        when(ridingLocationRepository.save(any(RidingLocation.class))).thenReturn(new RidingLocation());
        when(ridingLocationRepository.findNearbyUsers(anyString(),anyDouble(),anyDouble(),anyDouble())).thenReturn(nearbyUsers);

        // Mock memberRepository
        Member member = new Member();
        member.setId("test");
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(member));

        // Call the method
        ResponseDataDto<List<UserLocationResultDto>> result = ridingLocationService.saveLocationAndReturnNearbyUsers(memberId, longitude, latitude, packMode, targetSpeed, curSpeed);

        // Assertions
        assertEquals("OK : Result Count = 1", result.getMessage());
        assertEquals(200, result.getStatus());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testCheckDirtyMemoryRepository() {
        // Mock repositories
        when(ridingCoordinateRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        when(ridingSpeedRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));


        // Call the method
        ResponseNoDataDto result = ridingLocationService.checkDirtyMemoryRepository("testMemberId");

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());


    }
}
