package com.sba.recordingserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseNoDataDto {
    private String message;
    private int status;
    public ResponseNoDataDto(String response, int status)
    {
        this.message = response;
        this.status = status;
    }
}
