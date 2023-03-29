package com.example.webservice.utill;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseEntityFactory {
    public static ResponseEntity<BaseDto> createErrorResponseEntity(String errorDescription, HttpStatus httpStatus) {
        ErrorInfo errorInfo = new ErrorInfo(errorDescription, httpStatus.value());
        return new ResponseEntity<>(new BaseDto(errorInfo), httpStatus);

    }
}
