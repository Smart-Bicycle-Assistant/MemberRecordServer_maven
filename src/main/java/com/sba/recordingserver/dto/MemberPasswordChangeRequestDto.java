package com.sba.recordingserver.dto;


import com.sba.recordingserver.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MemberPasswordChangeRequestDto {
    private String id;
    private String password;
    private String newPassword;
}
