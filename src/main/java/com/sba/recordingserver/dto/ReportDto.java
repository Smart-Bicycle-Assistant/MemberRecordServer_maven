package com.sba.recordingserver.dto;

import com.sba.recordingserver.entity.Report;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Lob;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    @Column
    private String reporter;
    @Column private String target;
    @Column private Long time;
    @Lob
    private String content;

    public Report toEntity() {
        Report report = new Report();
        report.setReporter(reporter);
        report.setTarget(target);
        report.setTime(time);
        report.setContent(content);
        return report;
    }
}
