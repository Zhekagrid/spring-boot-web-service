package com.example.webservice.utill;

import com.example.webservice.model.dto.BaseDto;
import com.example.webservice.model.dto.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The ErrorResponseEntity factory
 */
public class ErrorResponseEntityFactory {
    /**
     * Create a {@link ResponseEntity} contains {@link ErrorInfo}
     *
     * @param errorDescription the message describing error
     * @param httpStatus       the {@link HttpStatus} for error
     * @return {@link ResponseEntity} containing {@link BaseDto} with errorInfo and {@link HttpStatus}
     */
    public static ResponseEntity<BaseDto> createErrorResponseEntity(String errorDescription, HttpStatus httpStatus) {
        ErrorInfo errorInfo = new ErrorInfo(errorDescription, httpStatus.value());
        return new ResponseEntity<>(new BaseDto(errorInfo), httpStatus);

    }
}
