package com.sba.recordingserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="riding_record")
@Getter
@Setter
@ToString
public class RidingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private String memberId;
    @Column private Integer bicycleNo;
    @Column private Long ridingTime;
    @Column private Double distance;
    @Column private Double avgSpeed;
    @Column private Double maxSpeed;
    @Column private Long ridingDuration;
    @Lob private String map;

}
