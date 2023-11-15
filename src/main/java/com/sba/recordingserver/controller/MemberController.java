package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
//@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;


//    @CrossOrigin(originPatterns = "http://localhost:3000")
    @PostMapping(value="/member/register")
    @Transactional
    public ResponseNoDataDto registerMember(@RequestBody MemberDto member) {
        return memberService.registerMember(member);
    }



    @PostMapping(value="/member/login")
    @Transactional
    public ResponseDataDto login(@RequestBody MemberLoginDto loginRequest) {
        System.out.println("login Reqeust");
        return memberService.handleLoginRequest(loginRequest);
    }

    @GetMapping(value="/member/id_available/{id}")
    @Transactional
    public ResponseNoDataDto checkDuplicateId(@PathVariable String id) {
        return memberService.checkDuplicateId(id);
    }

    @GetMapping(value="/member/reset_password/{id}")
    @Transactional
    public ResponseNoDataDto resetPassword(@PathVariable String id) {
        return memberService.resetPassword(id);
    }

    @PostMapping(value="/member/change_password")
    @Transactional
    public ResponseNoDataDto changePassword(@RequestBody MemberPasswordChangeRequestDto passwordChangeRequest) {
        return memberService.changePassword(passwordChangeRequest);
    }

    @DeleteMapping(value="/delete")
    @Transactional
    public ResponseNoDataDto deleteMember(@RequestHeader(value="Authorization")String token) {
        String memberId = TokenProvider.GetUserId(token.substring(token.lastIndexOf(" ")));
        return memberService.deleteMember(memberId);
    }
}
