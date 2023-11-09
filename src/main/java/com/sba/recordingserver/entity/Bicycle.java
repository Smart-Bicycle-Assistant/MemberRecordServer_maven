package com.sba.recordingserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="bicycle_list")
@Getter
@Setter
@ToString
public class Bicycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Long id;
    @Column private String ownerId;
    @Column private String bicycleName;
    @Lob private String bicycleImage;

}
