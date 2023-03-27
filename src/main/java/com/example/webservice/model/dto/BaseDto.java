package com.example.webservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BaseDto {

    private ErrorInfo errorInfo;

    public BaseDto(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public BaseDto() {
    }

}
