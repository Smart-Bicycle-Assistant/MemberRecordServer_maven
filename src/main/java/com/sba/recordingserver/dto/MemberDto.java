package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {
    private String id;
    private String password;
    private String nickname;
    private String email;

    public Member toEntity() {
        Member member = new Member();
        member.setId(this.id);
        member.setPassword(this.password);
        member.setNickname(this.nickname);
        member.setEmail(this.email);
        return member;
    }
}
