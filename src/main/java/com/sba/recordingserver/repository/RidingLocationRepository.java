package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.RidingLocation;
import com.sba.recordingserver.entity.RidingLocationWithDistance;
import com.sba.recordingserver.entity.RidingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.util.List;



public interface RidingLocationRepository extends JpaRepository<RidingLocation,String> {

    @Query(nativeQuery = true ,value="select l.id,l.longitude,l.latitude from ( select *, ST_Distance_Sphere(point(m.longitude,m.latitude),point(:longitude,:latitude)) as distance from riding_location m ) l where l.distance < 1000 and (l.id != :memberId) order by l.distance")
    List<RidingLocation> findNearbyUsers(@Param("memberId") String memberId, @Param("longitude") Double longitude, @Param("latitude") Double latitude);


//    @NamedNativeQuery(nativeQuery = true ,value="select * from ( select *, ST_Distance_Sphere(point(m.longitude,m.latitude),point(:longitude,:latitude)) as distance from riding_location m ) l where l.distance < 1000 and (l.id != :memberId) order by l.distance")

//    List<RidingLocationWithDistance> findNearbyUsersWithDistance(@Param("memberId") String memberId, @Param("longitude") Double longitude, @Param("latitude") Double latitude);
}
