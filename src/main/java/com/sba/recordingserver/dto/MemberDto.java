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
    public Member toNewEntity() {
        Member member = new Member();
        member.setId(this.id);
        member.setPassword(this.password);
        member.setNickname(this.nickname);
        member.setEmail(this.email);
        member.setBicycleNumber(0);
        member.setBanned(-1L);
        return member;
    }
}
