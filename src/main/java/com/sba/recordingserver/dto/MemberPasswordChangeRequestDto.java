package com.sba.recordingserver.dto;


import com.sba.recordingserver.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberPasswordChangeRequestDto {
    private String id;
    private String password;
    private String newPassword;
}
