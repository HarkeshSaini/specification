package com.specification.service.exception.impl;

import java.time.Instant;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.exception.ConflictException;
import com.specification.service.exception.GlobalUncheckedException;
import com.specification.service.exception.ResourceNotFoundException;
import com.specification.service.response.apires.APIFailureRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleBadCredentials(BadCredentialsException ex) {
		log.warn("{}: {}", ApplicationConstant.AUTHENTICATION_FAILED, ex.getMessage());
		ErrorResponse body = ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.toString()).message(ApplicationConstant.INVALID_CREDENTIALS).timestamp(Instant.now()).build();
        APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body, ApplicationConstant.ERROR_CODE_UNAUTHORIZED, String.valueOf(HttpStatus.UNAUTHORIZED.value())).getBody();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleAccessDenied(AccessDeniedException ex) {
		log.warn("{}: {}", ApplicationConstant.FORBIDDEN_MESSAGE, ex.getMessage());
		ErrorResponse body = ErrorResponse.builder().code(HttpStatus.FORBIDDEN.toString()).message(ex.getMessage() != null ? ex.getMessage() : ApplicationConstant.ACCESS_DENIED).timestamp(Instant.now()).build();
        APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body, ApplicationConstant.ERROR_CODE_FORBIDDEN, String.valueOf(HttpStatus.FORBIDDEN.value())).getBody();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleNotFound(ResourceNotFoundException ex) {
		log.warn("{}: {}", ApplicationConstant.RESOURCE_NOT_FOUND, ex.getMessage());
		ErrorResponse body = ErrorResponse.builder().code(HttpStatus.NOT_FOUND.toString()).message(ex.getMessage()).timestamp(Instant.now()).build();
        APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body, ApplicationConstant.ERROR_CODE_NOT_FOUND, String.valueOf(HttpStatus.NOT_FOUND.value())).getBody();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleConflict(ConflictException ex) {
		log.warn("Conflict: {}", ex.getMessage());
		ErrorResponse body = ErrorResponse.builder().code(HttpStatus.CONFLICT.toString()).message(ex.getMessage()).timestamp(Instant.now()).build();
        APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body, ApplicationConstant.ERROR_CODE_CONFLICT, String.valueOf(HttpStatus.CONFLICT.value())).getBody();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleBadRequest(IllegalArgumentException ex) {
		log.warn("Bad request: {}", ex.getMessage());
		ErrorResponse body = ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.toString()).message(ex.getMessage()).timestamp(Instant.now()).build();
        APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body, ApplicationConstant.ERROR_CODE_BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST.value())).getBody();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(GlobalUncheckedException.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleGlobalUnchecked(GlobalUncheckedException ex) {
		log.warn("Service error [{}]: {}", ex.getCode(), ex.getMessage());
		ErrorResponse body = ErrorResponse.builder().code(ex.getCode()).message(ex.getMessage()).timestamp(Instant.now()).build();
		APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body,ex.getCode(),String.valueOf(ex.getHttpStatus().value())).getBody();
		return ResponseEntity.status(ex.getHttpStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<APIFailureRes<ErrorResponse>> handleGeneric(Exception ex) {
		log.error("{}: {}", ApplicationConstant.UNHANDLED_ERROR, ex.getMessage(), ex);
		ErrorResponse body = ErrorResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).message(ApplicationConstant.UNEXPECTED_ERROR_OCCURRED).timestamp(Instant.now()).build();
        APIFailureRes<ErrorResponse> response = APIFailureRes.failureResponse(body, ApplicationConstant.UNEXPECTED_ERROR_OCCURRED, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())).getBody();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
