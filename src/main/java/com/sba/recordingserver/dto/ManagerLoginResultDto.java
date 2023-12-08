package com.sba.recordingserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ManagerLoginResultDto {
    private String id;
    private String nickname;
    private String email;
    private String token;
}
