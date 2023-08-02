package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.MemberDto;
import com.sba.recordingserver.dto.MemberLoginDto;
import com.sba.recordingserver.dto.MemberPasswordChangeRequestDto;
import com.sba.recordingserver.dto.ResponseDto;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

//    @CrossOrigin(originPatterns = "http://localhost:3000")
    @PostMapping(value="/register")
    @Transactional
    public ResponseDto registerMember(@RequestBody MemberDto member) {
        return memberService.registerMember(member);
    }

    @PostMapping(value="/login")
    @Transactional
    public ResponseDto login(@RequestBody MemberLoginDto loginRequest) {
        return memberService.handleLoginRequest(loginRequest);
    }

    @GetMapping(value="/id_available/{id}")
    @Transactional
    public ResponseDto checkDuplicateId(@PathVariable String id) {
        return memberService.checkDuplicateId(id);
    }

    @GetMapping(value="/reset_password/{id}")
    @Transactional
    public ResponseDto resetPassword(@PathVariable String id) {
        return memberService.resetPassword(id);
    }

    @PostMapping(value="/change_password")
    @Transactional
    public ResponseDto changePassword(@RequestBody MemberPasswordChangeRequestDto passwordChangeRequest) {
        return memberService.changePassword(passwordChangeRequest);
    }
}
