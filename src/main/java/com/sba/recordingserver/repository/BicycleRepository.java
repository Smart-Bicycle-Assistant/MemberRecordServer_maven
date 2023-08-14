package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.Bicycle;
import com.sba.recordingserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BicycleRepository extends JpaRepository<Bicycle,Long> {
    @Query("select m from Bicycle m where m.ownerId = :ownerId and m.bicycleName = :bicycleName")
    Optional<Bicycle> findBicycle(@Param("ownerId") String ownerId, @Param("bicycleName") String bicycleName);

}
