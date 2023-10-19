package com.sba.recordingserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="riding_location")
@Getter
@Setter
@ToString
@AllArgsConstructor
//@SqlResultSetMapping(
//        name="groupDetailsMapping",
//        classes={
//                @ConstructorResult(
//                        targetClass=RidingLocationWithDistance.class,
//                        columns={
//                                @ColumnResult(name="id"),
//                                @ColumnResult(name="longitude"),
//                                @ColumnResult(name="latitude"),
//                                @ColumnResult(name="distance")
//                        }
//                )
//        }
//)
//@NamedNativeQuery(name="findNearbyUsersWithDistance", query="select * from ( select *, ST_Distance_Sphere(point(m.longitude,m.latitude),point(:longitude,:latitude)) as distance from riding_location m ) l where l.distance < 1000 and (l.id != :memberId) order by l.distance", resultSetMapping="groupDetailsMapping")
public class RidingLocation {
    @Id
    @Column private String id;
    @Column private Double longitude;
    @Column private Double latitude;
    @Column private Double speed;


    public RidingLocation() {

    }

}
