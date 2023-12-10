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

    private String reporter;
    private String target;
    private Long time;
    private String content;

    public Report toEntity() {
        Report report = new Report();
        report.setReporter(reporter);
        report.setTarget(target);
        report.setTime(time);
        report.setContent(content);
        report.setSolved(0);
        return report;
    }
}
