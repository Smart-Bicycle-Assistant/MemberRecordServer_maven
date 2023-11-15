package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.entity.RidingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RidingRecordRepository extends JpaRepository<RidingRecord,Long> {
    @Query("select m from RidingRecord m where m.bicycleId = :bicycleNo")
    List<RidingRecord> findMatchingRecord(@Param("bicycleNo") Long bicycleId);

    @Query("select m from RidingRecord m where m.bicycleId = :bicycleNo and m.ridingTime > :time")
    List<RidingRecord> findMatchingRecordAfter(@Param("bicycleNo") Long bicycleId, @Param("time") Long time);

    @Query("select SUM(m.distance) from RidingRecord m where m.bicycleId = :id")
    Double findSumOfTargetBicycle(@Param("id") Long id);

    List<RidingRecord> deleteAllByMemberId(String memberId);
    List<RidingRecord> deleteAllByBicycleId(Long bicycleId);

    List<RidingRecord> findAllByMemberId(String memberId);
}
