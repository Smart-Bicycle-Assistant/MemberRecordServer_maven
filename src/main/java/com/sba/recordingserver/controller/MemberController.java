package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;


//    @CrossOrigin(originPatterns = "http://localhost:3000")
    @PostMapping(value="/register")
    @Transactional
    public ResponseNoDataDto registerMember(@RequestBody MemberDto member) {
        return memberService.registerMember(member);
    }



    @PostMapping(value="/login")
    @Transactional
    public ResponseDataDto login(@RequestBody MemberLoginDto loginRequest) {
        System.out.println("login Reqeust");
        return memberService.handleLoginRequest(loginRequest);
    }

    @GetMapping(value="/id_available/{id}")
    @Transactional
    public ResponseNoDataDto checkDuplicateId(@PathVariable String id) {
        return memberService.checkDuplicateId(id);
    }

    @GetMapping(value="/reset_password/{id}")
    @Transactional
    public ResponseNoDataDto resetPassword(@PathVariable String id) {
        return memberService.resetPassword(id);
    }

    @PostMapping(value="/change_password")
    @Transactional
    public ResponseNoDataDto changePassword(@RequestBody MemberPasswordChangeRequestDto passwordChangeRequest) {
        return memberService.changePassword(passwordChangeRequest);
    }
}
