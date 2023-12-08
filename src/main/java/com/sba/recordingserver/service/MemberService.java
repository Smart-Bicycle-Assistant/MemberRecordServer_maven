package com.sba.recordingserver.service;

import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.Bicycle;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.repository.*;
import com.sba.recordingserver.security.PasswordEncoder;
import com.sba.recordingserver.security.TokenProvider;
import com.sba.recordingserver.util.Util;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final BicycleRepository bicycleRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RidingRecordRepository ridingRecordRepository;

    @Autowired
    private RidingLocationRepository ridingLocationRepository;

    @Autowired
    private ManagementRecordRepository managementRecordRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
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
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            memberRepository.save(member.toNewEntity());
            System.out.println("register done");
            return new ResponseNoDataDto("Register Success",200);
        }
    }



    public ResponseDataDto<MemberLoginResultDto> handleLoginRequest(MemberLoginDto loginRequest) {
        Optional<Member> optionalMember = memberRepository.findById(loginRequest.getId());
        MemberLoginResultDto memberLoginResultDto = null;
        if(optionalMember.isEmpty())
        {
            System.out.println(loginRequest.getId() + " is not in member db");
            return new ResponseDataDto("no such id",406, null);
        }

        else if(!passwordEncoder.matches(loginRequest.getPassword(),optionalMember.get().getPassword()))
        {
            System.out.println("invalid password " + optionalMember.get().getPassword() + " : " + loginRequest.getPassword());
            return new ResponseDataDto("invalid password",406,null);
        }
        else
        {
            Long currentTime = System.currentTimeMillis();
            Member member = optionalMember.get();
            System.out.println(member.getBanned());
            System.out.println(currentTime);
            if(member.getBanned() == -1) {
                System.out.println("login request for "+loginRequest.getId() + " successfully done");
                final String token = tokenProvider.create(loginRequest.toEntity());
                return new ResponseDataDto("OK",200,new MemberLoginResultDto(optionalMember.get().getId(),optionalMember.get().getNickname(),optionalMember.get().getEmail(),token));
            }
            else if(member.getBanned() < currentTime) {
                member.setBanned(-1L);
                memberRepository.save(member);
                System.out.println("remove ban");
                System.out.println("login request for "+loginRequest.getId() + " successfully done");
                final String token = tokenProvider.create(loginRequest.toEntity());
                return new ResponseDataDto("OK",200,new MemberLoginResultDto(optionalMember.get().getId(),optionalMember.get().getNickname(),optionalMember.get().getEmail(),token));
            }
            else {
                System.out.println("this guy is banned");
                return new ResponseDataDto("you are banned until " + member.getBanned(),403,null);
            }

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
            message.setText("Your request for reseting your password to account <" + optionalMember.get().getId() + ">has been successfully done\n" +
                    "Your new password is " +password + "\n"+
                    "Please change your password immediately when you login\n"+
                    "Thank you\n");
            Member member = optionalMember.get();
            member.setPassword(passwordEncoder.encode(password));
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

            if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) //wrong password
            {
                System.out.println("invalid password " + optionalMember.get().getPassword() + " : " + request.getPassword());
                return new ResponseNoDataDto("invalid password",406);
            }
            else if(passwordEncoder.matches(request.getNewPassword(), member.getPassword())) //new password old password same
            {
                System.out.println("new password old password same" + optionalMember.get().getPassword() + " : " + request.getNewPassword());
                return new ResponseNoDataDto("new password is same as before",406);
            }
            else
            {
                member.setPassword(passwordEncoder.encode( request.getNewPassword()));
                memberRepository.save(member);
                return new ResponseNoDataDto("your new password has been successfully changed",200);
            }
        }
    }


    public ResponseNoDataDto deleteMember(String memberId) {
        if(bicycleRepository.findAllByOwnerIdOrderById(memberId).size() != 0) {
            List<Bicycle> bicycleList =  bicycleRepository.deleteAllByOwnerId(memberId);

        }
        managementRecordRepository.deleteAllByMemberId(memberId);
        if(memberRepository.existsById(memberId)) {
            ridingRecordRepository.deleteAllByMemberId(memberId);
        }
        if(ridingLocationRepository.findById(memberId).isPresent()) {
            ridingLocationRepository.deleteById(memberId);
        }



        memberRepository.deleteMemberById(memberId);
        return new ResponseNoDataDto("OK",200);
    }

    public ResponseDataDto getUserInfo(String memberId) {
        Optional<Member> member =  memberRepository.findById(memberId);
        if(member.isPresent()) {
            return new ResponseDataDto<UserInfoDto>("OK",200,new UserInfoDto(member.get().getId(),member.get().getNickname(),member.get().getEmail()));
        }
        else
        {
            return new ResponseDataDto("No such member",204,null);
        }
    }

    public ResponseNoDataDto updateMemberData(String memberId, MemberDataUpdateDto memberDto) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(!memberId.equals(memberDto.getId())) {
            return new ResponseNoDataDto("trying to change id",403);
        }
        else {
            if(optionalMember.isEmpty()) {
                return new ResponseNoDataDto("no such member with that id",204);
            }
            else {
                Member member = optionalMember.get();
                member.setNickname(memberDto.getNickname());
                member.setEmail(memberDto.getEmail());
                memberRepository.save(member);
                return new ResponseNoDataDto("OK",200);
            }
        }
    }


}
