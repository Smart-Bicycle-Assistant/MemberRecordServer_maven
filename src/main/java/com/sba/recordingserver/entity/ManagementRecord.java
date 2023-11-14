package com.sba.recordingserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "management_record")
@ToString
public class ManagementRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private String memberId;
    @Column private Long bicycleId;
    @Column private Integer frontTire;
    @Column private Integer rearTire;
    @Column private Integer frontTireLife;
    @Column private Integer rearTireLife;
    @Column private Integer brakes;
    @Column private Integer chain;
    @Column private Integer gears;
    @Column private Long managementTime;
    public static final Integer CHANGED = 2;
    public static final Integer CHECKED = 1;
    public static final Integer NOTHING = 0;
}
