package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.Bicycle;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.repository.BicycleRepository;
import com.sba.recordingserver.repository.MemberRepository;
import com.sba.recordingserver.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final BicycleRepository bicycleRepository;

    private JavaMailSender emailSender;



    public ResponseNoDataDto registerMember(MemberDto member) {
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        if(optionalMember.isPresent())
        {
            System.out.println(member.getId()+" is already taken");
            return new ResponseNoDataDto("That id is already taken", 406);
        }
        else
        {
            memberRepository.save(member.toNewEntity());
            System.out.println("register done");
            return new ResponseNoDataDto("Register Success",200);
        }
    }



    public ResponseNoDataDto handleLoginRequest(MemberLoginDto loginRequest) {
        Optional<Member> optionalMember = memberRepository.findById(loginRequest.getId());
        if(optionalMember.isEmpty())
        {
            System.out.println(loginRequest.getId() + " is not in member db");
            return new ResponseNoDataDto("no such id",406);
        }
        else if(!optionalMember.get().getPassword().equals(loginRequest.getPassword()))
        {
            System.out.println("invalid password " + optionalMember.get().getPassword() + " : " + loginRequest.getPassword());
            return new ResponseNoDataDto("invalid password",406);
        }
        else
        {
            System.out.println("login request for "+loginRequest.getId() + " successfully done");
            return new ResponseNoDataDto("welcome " +optionalMember.get().getNickname(),200);
        }
    }

    public ResponseNoDataDto checkDuplicateId(String id)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isEmpty())
        {
            System.out.println(id + " is not in member db");
            return new ResponseNoDataDto("OK",200);
        }
        else
        {
            System.out.println(id + " is already taken");
            return new ResponseNoDataDto("duplicate",406);
        }
    }

    public ResponseNoDataDto resetPassword(String id)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isEmpty())
        {
            System.out.println(id+" is not in member db");
            return new ResponseNoDataDto("no such id",406);
        }
        else
        {
            String password = Util.createPassword();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ywha0929@gmail.com");
            message.setTo(optionalMember.get().getEmail());
            message.setSubject("You've reseted your password");
            message.setText("Your request for reseting your password has been successfully done\n" +
                    "Your new password is " +password + "\n"+
                    "Please change your password immediately when you login\n"+
                    "Thank you\n");
            Member member = optionalMember.get();
            member.setPassword(password);
            memberRepository.save(member);
            emailSender.send(message);
            return new ResponseNoDataDto("Your new password has been sent to your email",200);
        }
    }

    public ResponseNoDataDto changePassword(MemberPasswordChangeRequestDto request)
    {
        Optional<Member> optionalMember = memberRepository.findById(request.getId());
        if(optionalMember.isEmpty())
        {
            System.out.println(request.getId()+" is not in member db");
            return new ResponseNoDataDto("no such id",406);
        }
        else
        {
            Member member = optionalMember.get();
            if(!member.getPassword().equals(request.getPassword())) //wrong password
            {
                System.out.println("invalid password " + optionalMember.get().getPassword() + " : " + request.getPassword());
                return new ResponseNoDataDto("invalid password",406);
            }
            else if(member.getPassword().equals(request.getNewPassword())) //new password old password same
            {
                System.out.println("new password old password same" + optionalMember.get().getPassword() + " : " + request.getNewPassword());
                return new ResponseNoDataDto("new password is same as before",406);
            }
            else
            {
                member.setPassword(request.getNewPassword());
                memberRepository.save(member);
                return new ResponseNoDataDto("your new password has been successfully changed",200);
            }
        }
    }

    public ResponseNoDataDto registerBicycle(BicycleRegisterRequestDto request)
    {
        Optional<Member> optOwner =  memberRepository.findById(request.getOwnerId());
        if(optOwner.isEmpty())
        {
            return new ResponseNoDataDto("there is no such user id",406);
        }
        Optional<Bicycle> result =  bicycleRepository.findBicycle(request.getOwnerId(), request.getBicycleName());
        if(result.isEmpty())
        {
            Member owner = optOwner.get();
            owner.setBicycleNumber(owner.getBicycleNumber()+1);
            memberRepository.save(owner);
            bicycleRepository.save(request.toEntity());
            return new ResponseNoDataDto("Register Success",200);
        }
        else
        {
            return new ResponseNoDataDto("owner has bicycle with same name",406);
        }
    }
}
