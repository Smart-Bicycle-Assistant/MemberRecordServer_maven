package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.ManagementRecord;
import com.sba.recordingserver.entity.RidingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagementRecordRepository extends JpaRepository<ManagementRecord,Long> {

    @Query("select m from ManagementRecord m where m.memberId = :memberId and m.bicycleId = :bicycleId")
    List<ManagementRecord> findMatchingRecord(@Param("memberId") String memberId, @Param("bicycleId") Long bicycleId);
}
