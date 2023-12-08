package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.Manager;
import com.sba.recordingserver.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ManagerDto {
    private String id;
    private String password;
    private String nickname;
    private String email;

    public Manager toEntity() {
        Manager member = new Manager();
        member.setId(this.id);
        member.setPassword(this.password);
        member.setNickname(this.nickname);
        member.setEmail(this.email);
        return member;
    }
    public Manager toNewEntity() {
        Manager member = new Manager();
        member.setId(this.id);
        member.setPassword(this.password);
        member.setNickname(this.nickname);
        member.setEmail(this.email);
        return member;
    }
}
