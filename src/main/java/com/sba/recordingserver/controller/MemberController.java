package com.sba.recordingserver.controller;

import com.sba.recordingserver.dto.MemberDto;
import com.sba.recordingserver.dto.MemberLoginDto;
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

    @PostMapping(value="/register")
    @Transactional
    public String registerMember(@RequestBody MemberDto member) {
        return memberService.registerMember(member);
    }

    @GetMapping(value="/login")
    @Transactional
    public String login(@RequestBody MemberLoginDto loginRequest) {
        return memberService.handleLoginRequtest(loginRequest);
    }

    @GetMapping(value="/id_available/{id}")
    @Transactional
    public String checkDuplicateId(@PathVariable String id) {
        return memberService.checkDuplicateId(id);
    }
}
