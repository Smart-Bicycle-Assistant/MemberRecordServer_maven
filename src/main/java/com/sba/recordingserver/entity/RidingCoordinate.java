package com.sba.recordingserver.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.List;

@Entity
@Getter
@Setter
public class RidingCoordinate {
    @Id
    @Column
    private String id;
    @Lob
    private String latitude;
    @Lob String longitude;

}
