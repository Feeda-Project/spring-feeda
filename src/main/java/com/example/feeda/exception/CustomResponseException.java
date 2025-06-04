package com.example.feeda.exception;

import com.example.feeda.exception.enums.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomResponseException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorMessage;

    public CustomResponseException(ResponseError responseError) {
        this.httpStatus = responseError.getHttpStatus();
        this.errorMessage = responseError.getMessage();
    }
}