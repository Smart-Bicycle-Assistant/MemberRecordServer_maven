package com.sba.recordingserver.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor

public class RidingLocationWithDistance {

    @Id
    @Column
    private String id;
    @Column private Double longitude;
    @Column private Double latitude;
    @Column private Double distance;
    public RidingLocationWithDistance() {

    }
}
