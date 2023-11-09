package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.RidingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface RidingLocationRepository extends JpaRepository<RidingLocation,String> {

    @Query(nativeQuery = true ,value="select l.id,l.longitude,l.latitude,l.speed from ( select *, ST_Distance_Sphere(point(m.longitude,m.latitude),point(:longitude,:latitude)) as distance from riding_location m ) l where l.distance < 100000 and (l.id != :memberId) and l.speed = :speed order by l.distance")
    List<RidingLocation> findNearbyUsers(@Param("memberId") String memberId, @Param("longitude") Double longitude, @Param("latitude") Double latitude, @Param("speed") Double speed);


//    @NamedNativeQuery(nativeQuery = true ,value="select * from ( select *, ST_Distance_Sphere(point(m.longitude,m.latitude),point(:longitude,:latitude)) as distance from riding_location m ) l where l.distance < 1000 and (l.id != :memberId) order by l.distance")

//    List<RidingLocationWithDistance> findNearbyUsersWithDistance(@Param("memberId") String memberId, @Param("longitude") Double longitude, @Param("latitude") Double latitude);
}
