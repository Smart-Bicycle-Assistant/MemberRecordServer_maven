package com.sba.recordingserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="report")
@Getter
@Setter
@ToString
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private String reporter;
    @Column private String target;
    @Column private Long time;
    @Lob private String content;
}
