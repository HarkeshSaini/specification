package com.specification.service.exception;

import lombok.Generated;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class GlobalUncheckedException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;
    private final String category;
    private final String severity;
    private final String message;
    private final Map<String, Object> errorContext;

    public GlobalUncheckedException(String code, String message, HttpStatus httpStatus, String category, String severity) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.category = category;
        this.severity = severity;
        this.message = message;
        this.errorContext = null;
    }

    public GlobalUncheckedException(String code, String message, HttpStatus httpStatus, String category, String severity, Map<String, Object> errorContext) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.category = category;
        this.severity = severity;
        this.message = message;
        this.errorContext =  errorContext;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Generated
    public String getCategory() {
        return this.category;
    }

    @Generated
    public String getSeverity() {
        return this.severity;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public Map<String, Object> getErrorContext() {
        return this.errorContext;
    }
}

