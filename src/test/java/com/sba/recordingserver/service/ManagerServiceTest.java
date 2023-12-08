package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.Manager;
import com.sba.recordingserver.repository.ManagerRepository;
import com.sba.recordingserver.security.PasswordEncoder;
import com.sba.recordingserver.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ManagerService managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterMember() {
        // Mock the repository response
        when(managerRepository.findById(any())).thenReturn(Optional.empty());

        // Mock the password encoding
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");

        // Create a mock ManagerDto
        ManagerDto managerDto = new ManagerDto("manager2","manager1234","admin","manager@aou.ac.kr");

        // Call the method
        ResponseNoDataDto result = managerService.registerMember(managerDto);

        // Assertions
        assertEquals("Register Success", result.getMessage());
        assertEquals(200, result.getStatus());
    }

    @Test
    void testHandleLoginRequest() {
        // Mock the repository response
        when(managerRepository.findById(any())).thenReturn(Optional.of(new Manager()));

        // Mock the password matching
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // Mock the token creation
        when(tokenProvider.create(any())).thenReturn("mockedToken");

        // Create a mock ManagerLoginDto
        ManagerLoginDto loginDto = new ManagerLoginDto();

        // Call the method
        ResponseDataDto<ManagerLoginResultDto> result = managerService.handleLoginRequest(loginDto);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
        // Add more assertions based on the expected result
    }

    @Test
    void testCheckDuplicateId() {
        // Mock the repository response
        when(managerRepository.findById(any())).thenReturn(Optional.empty());

        // Create a mock ID
        String id = "testId";

        // Call the method
        ResponseNoDataDto result = managerService.checkDuplicateId(id);

        // Assertions
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }
}
