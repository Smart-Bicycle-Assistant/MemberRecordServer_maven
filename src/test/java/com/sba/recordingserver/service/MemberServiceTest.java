package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.repository.ManagementRecordRepository;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.repository.RidingLocationRepository;
import com.sba.recordingserver.repository.RidingRecordRepository;
import com.sba.recordingserver.security.PasswordEncoder;
import com.sba.recordingserver.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = { TestConfiguration.class }, loader = AnnotationConfigContextLoader.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

//    @Autowired
    @Mock
    private TokenProvider tokenProvider;

//    @Autowired
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RidingRecordRepository ridingRecordRepository;

    @Mock
    private RidingLocationRepository ridingLocationRepository;

    @Mock
    private ManagementRecordRepository managementRecordRepository;

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private MemberService memberService ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterMember() {
        // Arrange
        MemberDto memberDto = new MemberDto("test","test1234","nick","test1234@email.com");
        // Act
        ResponseNoDataDto response = memberService.registerMember(memberDto);

        // Assert
        assertEquals("Register Success", response.getMessage());
        assertEquals(200, response.getStatus());
        verify(memberRepository, times(1)).findById(any());
        verify(memberRepository, times(1)).save(any());
    }

    // Add more test methods for other functionalities in MemberService
    // ...

    @Test
    void testHandleLogin() {
        // Arrange
        MemberDto memberDto = new MemberDto("test", "test1234", "nick", "test1234@email.com");


        // Mock the behavior of memberRepository and passwordEncoder
        when(memberRepository.findById(any())).thenReturn(Optional.of(memberDto.toNewEntity()));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(tokenProvider.create(any())).thenReturn("mockedToken");

        // Act
        ResponseNoDataDto registerResponse = memberService.registerMember(memberDto);
        System.out.println(registerResponse.getMessage());

        MemberLoginDto loginDto = new MemberLoginDto("test", "test1234");
        ResponseDataDto<MemberLoginResultDto> result = memberService.handleLoginRequest(loginDto);

        // Assert
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());

        MemberLoginResultDto resultData = result.getData();
        assertEquals("nick", resultData.getNickname());
        assertEquals("test", resultData.getId());
        assertEquals("test1234@email.com", resultData.getEmail());
    }

    @Test
    void testCheckDuplicateId() {
        // Arrange
        String testId = "testId";

        // Mock the behavior of memberRepository
        when(memberRepository.findById(any())).thenReturn(Optional.empty()); // Simulate that the ID is not present in the repository

        // Act
        ResponseNoDataDto result = memberService.checkDuplicateId(testId);

        // Assert
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());
    }

    @Test
    void testCheckDuplicateIdDuplicate() {
        // Arrange
        String existingId = "existingId";

        // Mock the behavior of memberRepository
        when(memberRepository.findById(existingId)).thenReturn(Optional.of(new Member())); // Simulate that the ID is already present in the repository

        // Act
        ResponseNoDataDto result = memberService.checkDuplicateId(existingId);

        // Assert
        assertEquals("duplicate", result.getMessage());
        assertEquals(406, result.getStatus());
    }

    @Test
    void testResetPassword() {
        // Arrange
        String testId = "testId";
        Member testMember = new Member();
        testMember.setId(testId);
        testMember.setEmail("test@example.com");

        // Mock the behavior of memberRepository
        when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));
        when(passwordEncoder.encode(any())).thenReturn("newEncodedPassword");

        // Mock the behavior of emailSender
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        // Act
        ResponseNoDataDto result = memberService.resetPassword(testId);

        // Assert
        assertEquals("Your new password has been sent to your email", result.getMessage());
        assertEquals(200, result.getStatus());

        // Verify that the member's password was updated and the email was sent
        verify(passwordEncoder, times(1)).encode(any());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testChangePassword() {
        // Arrange
        String testId = "testId";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        Member testMember = new Member();
        testMember.setId(testId);
        testMember.setPassword(passwordEncoder.encode(oldPassword));

        // Mock the behavior of memberRepository
        when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));
        when(passwordEncoder.matches(oldPassword, testMember.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(newPassword, testMember.getPassword())).thenReturn(false);

        // Act
        ResponseNoDataDto result = memberService.changePassword(new MemberPasswordChangeRequestDto(testId, oldPassword, newPassword));

        // Assert
        assertEquals("your new password has been successfully changed", result.getMessage());
        assertEquals(200, result.getStatus());

        // Verify that the member's password was updated
        verify(passwordEncoder, times(1)).matches(oldPassword, testMember.getPassword());
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testGetUserInfo() {
        // Arrange
        String testId = "testId";
        Member testMember = new Member();
        testMember.setId(testId);
        testMember.setNickname("testNickname");
        testMember.setEmail("test@example.com");

        // Mock the behavior of memberRepository
        when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));

        // Act
        ResponseDataDto<UserInfoDto> result = memberService.getUserInfo(testId);

        // Assert
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());

        UserInfoDto userInfo = result.getData();
        assertEquals(testId, userInfo.getId());
        assertEquals("testNickname", userInfo.getNickname());
        assertEquals("test@example.com", userInfo.getEmail());

        // Verify that memberRepository.findById was called
        verify(memberRepository, times(1)).findById(testId);
    }

    @Test
    void testUpdateMemberData() {
        // Arrange
        String testId = "testId";
        MemberDataUpdateDto updateDto = new MemberDataUpdateDto();
        updateDto.setId(testId);
        updateDto.setNickname("newNickname");
        updateDto.setEmail("new@example.com");

        Member testMember = new Member();
        testMember.setId(testId);
        testMember.setNickname("oldNickname");
        testMember.setEmail("old@example.com");

        // Mock the behavior of memberRepository
        when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));

        // Act
        ResponseNoDataDto result = memberService.updateMemberData(testId, updateDto);

        // Assert
        assertEquals("OK", result.getMessage());
        assertEquals(200, result.getStatus());

        // Verify that the member's data was updated
        verify(memberRepository, times(1)).findById(testId);
        verify(memberRepository, times(1)).save(any(Member.class));

        // Check the updated values
        assertEquals(updateDto.getNickname(), testMember.getNickname());
        assertEquals(updateDto.getEmail(), testMember.getEmail());
    }
}
