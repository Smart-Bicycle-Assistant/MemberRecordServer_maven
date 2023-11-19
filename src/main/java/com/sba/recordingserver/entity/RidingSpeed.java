package com.sba.recordingserver.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;


@Entity
@Getter
@Setter
public class RidingSpeed {

    @Id
    @Column
    private String id;
    @Lob
    private String speed;

}
