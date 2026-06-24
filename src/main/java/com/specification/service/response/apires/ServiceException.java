package com.specification.service.response.apires;

import com.specification.service.exception.ErrorConstant;
import com.specification.service.exception.GlobalUncheckedException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ServiceException extends GlobalUncheckedException {
    public ServiceException(String code, String message, ErrorConstant.CATEGORY category, ErrorConstant.SEVERITY severity) {
        super(code, message, HttpStatus.BAD_REQUEST, category.toString(), severity.toString());
    }

    public ServiceException(String code, String message, ErrorConstant.CATEGORY category, ErrorConstant.SEVERITY severity, Map<String, Object> errorContext) {
        super(code, message, HttpStatus.BAD_REQUEST, category.toString(), severity.toString(), errorContext);
    }

    public ServiceException(String code, String message, ErrorConstant.CATEGORY category, ErrorConstant.SEVERITY severity, HttpStatus httpStatus) {
        super(code, message, httpStatus, category.toString(), severity.toString());
    }

    public ServiceException(String code, String message, ErrorConstant.CATEGORY category, ErrorConstant.SEVERITY severity, HttpStatus httpStatus, Map<String, Object> errorContext) {
        super(code, message, httpStatus, category.toString(), severity.toString(), errorContext);
    }
}
