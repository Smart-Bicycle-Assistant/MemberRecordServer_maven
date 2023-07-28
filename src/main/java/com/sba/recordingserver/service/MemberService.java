package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.MemberDto;
import com.sba.recordingserver.dto.MemberLoginDto;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    public String registerMember(MemberDto member) {
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        if(optionalMember.isPresent())
        {
            System.out.println(member.getId()+" is already taken");
            return "That id is already taken";
        }
        else
        {
            memberRepository.save(member.toEntity());
            System.out.println("register done");
            return "Register Success";
        }
    }


    public String handleLoginRequtest(MemberLoginDto loginRequest) {
        Optional<Member> optionalMember = memberRepository.findById(loginRequest.getId());
        if(optionalMember.isEmpty())
        {
            System.out.println(loginRequest.getId() + " is not in member db");
            return "no such id";
        }
        else if(!optionalMember.get().getPassword().equals(loginRequest.getPassword()))
        {
            System.out.println("invalid password " + optionalMember.get().getPassword() + " : " + loginRequest.getPassword());
            return "invalid password";
        }
        else
        {
            System.out.println("login request for "+loginRequest.getId() + " successfully done");
            return "welcome " +optionalMember.get().getNickname();
        }
    }

    public String checkDuplicateId(String id)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isEmpty())
        {
            System.out.println(id + " is not in member db");
            return "ok";
        }
        else
        {
            System.out.println(id + " is already taken");
            return "duplicate";
        }
    }
}
