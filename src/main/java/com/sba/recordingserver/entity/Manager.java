package com.sba.recordingserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="manager")
@Getter
@Setter
@ToString
public class Manager {
    @Id
    @Column
    private String id;
    @Column private String password;
    @Column private String nickname;
    @Column private String email;
}
