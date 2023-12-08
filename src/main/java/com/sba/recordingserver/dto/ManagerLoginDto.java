package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
