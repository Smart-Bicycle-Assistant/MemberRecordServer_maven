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
public class ManagerLoginDto {
    private String id;
    private String password;

    public Member toEntity() {
        Member member = new Member();
        member.setId(this.id);
        member.setPassword(this.password);
        return member;
    }
}
