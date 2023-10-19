package com.sba.recordingserver.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemberLoginResultDto {
    private String id;
    private String nickname;
    private String email;
    private String token;
}
