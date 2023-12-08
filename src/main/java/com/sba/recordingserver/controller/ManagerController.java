package com.sba.recordingserver.controller;


import com.sba.recordingserver.dto.*;
import com.sba.recordingserver.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
public class ManagerController {
    @Autowired
    ManagerService managerService;


    @PostMapping(value="/manager/register")
    @Transactional
    public ResponseNoDataDto registerMember(@RequestBody ManagerDto manager) {
        return managerService.registerMember(manager);
    }



    @PostMapping(value="/manager/login")
    @Transactional
    public ResponseDataDto login(@RequestBody ManagerLoginDto loginRequest) {
        System.out.println("login Reqeust");
        return managerService.handleLoginRequest(loginRequest);
    }

    @GetMapping(value="/manager/id_available/{id}")
    @Transactional
    public ResponseNoDataDto checkDuplicateId(@PathVariable String id) {
        return managerService.checkDuplicateId(id);
    }
}
