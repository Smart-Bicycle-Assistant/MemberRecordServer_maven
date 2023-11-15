package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.ManagementRecord;
import com.sba.recordingserver.entity.RidingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagementRecordRepository extends JpaRepository<ManagementRecord,Long> {

    @Query("select m from ManagementRecord m where m.memberId = :memberId and m.bicycleId = :bicycleId order by m.managementTime desc")
    List<ManagementRecord> findMatchingRecord(@Param("memberId") String memberId, @Param("bicycleId") Long bicycleId);

    Optional<ManagementRecord> findTopByBicycleIdAndFrontTireOrderByManagementTimeDesc(@Param("bicycleId") Long bicycleId, @Param("frontTire") Integer frontTire);

    Optional<ManagementRecord> findTopByBicycleIdAndRearTireOrderByManagementTimeDesc(@Param("bicycleId") Long bicycleId, @Param("rearTire") Integer rearTire);

    Optional<ManagementRecord> findTopByBicycleIdAndBrakesOrderByManagementTimeDesc(@Param("bicycleId") Long bicycleId, @Param("brakes") Integer brakes);

    Optional<ManagementRecord> findTopByBicycleIdAndChainOrderByManagementTimeDesc(@Param("bicycleId") Long bicycleId, @Param("brakes") Integer chain);

    Optional<ManagementRecord> findTopByBicycleIdAndGearsOrderByManagementTimeDesc(@Param("bicycleId") Long bicycleId, @Param("brakes") Integer gears);

    List<ManagementRecord> deleteAllByBicycleId(@Param("bicycleId") Long bicycleId);

    List<ManagementRecord> deleteAllByMemberId(String memberId);

}
