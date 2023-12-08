package com.sba.recordingserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member {
    @Id @Column private String id;
    @Column private String password;
    @Column private String nickname;
    @Column private String email;
    @Column private Integer bicycleNumber;
    @Column private Long banned;

}
