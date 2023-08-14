package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.Member;
import com.sba.recordingserver.entity.RidingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RidingRecordRepository extends JpaRepository<RidingRecord,Long> {
    @Query("select m from RidingRecord m where m.memberId = :memberId and m.bicycleNo = :bicycleNo")
    List<RidingRecord> findMatchingRecord(@Param("memberId") String memberId, @Param("bicycleNo") Integer bicycleNo);

    @Query("select m from RidingRecord m where m.memberId = :memberId and m.bicycleNo = :bicycleNo and m.ridingTime > :time")
    List<RidingRecord> findMatchingRecordAfter(@Param("memberId") String memberId, @Param("bicycleNo") Integer bicycleNo, @Param("time") Long time);
}
