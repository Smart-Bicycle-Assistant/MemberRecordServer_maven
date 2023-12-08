package com.sba.recordingserver.service;


import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.entity.Manager;
import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.repository.ManagerRepository;
import com.sba.recordingserver.security.PasswordEncoder;
import com.sba.recordingserver.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagerService {
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseNoDataDto registerMember(ManagerDto manager) {

        Optional<Manager> optionalManager = managerRepository.findById(manager.getId());
        if(optionalManager.isPresent())
        {
            System.out.println(manager.getId()+" is already taken");
            return new ResponseNoDataDto("That id is already taken", 406);
        }
        else
        {
            manager.setPassword(passwordEncoder.encode(manager.getPassword()));
            managerRepository.save(manager.toNewEntity());
            System.out.println("register done");
            return new ResponseNoDataDto("Register Success",200);
        }
    }



    public ResponseDataDto<ManagerLoginResultDto> handleLoginRequest(ManagerLoginDto loginRequest) {
        Optional<Manager> optionalMember = managerRepository.findById(loginRequest.getId());
        ManagerLoginResultDto memberLoginResultDto = null;
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
            System.out.println("login request for "+loginRequest.getId() + " successfully done");
            final String token = tokenProvider.create(loginRequest.toEntity());
            return new ResponseDataDto("OK",200,new MemberLoginResultDto(optionalMember.get().getId(),optionalMember.get().getNickname(),optionalMember.get().getEmail(),token));
        }
    }

    public ResponseNoDataDto checkDuplicateId(String id)
    {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if(optionalManager.isEmpty())
        {
            System.out.println(id + " is not in manager db");
            return new ResponseNoDataDto("OK",200);
        }
        else
        {
            System.out.println(id + " is already taken");
            return new ResponseNoDataDto("duplicate",406);
        }
    }
}
