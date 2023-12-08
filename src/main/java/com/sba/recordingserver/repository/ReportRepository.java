package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {
    List<Report> findAllByOrderByTimeDesc();
    List<Report> findAllByReporter(String reporterId);
    List<Report> findAllByTarget(String targetId);

    @Query(nativeQuery = false ,value="select m from Report m where 2 < (select count(k.target) from Report k where k.target = m.target)")
//    @Query(nativeQuery = true, value="select l.id,l.reporter,l.target,l.time ,l.content from (select *, count(k.target) as num from report k) l where l.num > 2 order by l.num")
    List<Report> findAllThoseReportedMoreThanThreeTimes();


}
