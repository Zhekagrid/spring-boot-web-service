package com.example.webservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInfo {
    private String errorDescription;
    private Integer errorCode;

    public ErrorInfo(String errorDescription, Integer errorCode) {
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }



}
