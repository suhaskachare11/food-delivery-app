package com.quickbite.fooddelivery.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.postgresql.util.PSQLException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
                        EmailAlreadyExistsException exception) {

                return buildResponse(
                                HttpStatus.CONFLICT,
                                exception.getErrorCode(),
                                exception.getMessage());
        }

        @ExceptionHandler(PhoneAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handlePhoneAlreadyExists(
                        PhoneAlreadyExistsException exception) {

                return buildResponse(
                                HttpStatus.CONFLICT,
                                exception.getErrorCode(),
                                exception.getMessage());
        }

        @ExceptionHandler(PasswordMismatchException.class)
        public ResponseEntity<ErrorResponse> handlePasswordMismatch(
                        PasswordMismatchException exception) {

                return buildResponse(
                                HttpStatus.BAD_REQUEST,
                                exception.getErrorCode(),
                                exception.getMessage());
        }

        private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, ErrorCode errorCode, String message) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .success(false)
                                .code(errorCode.getCode())
                                .message(message)
                                .errors(Collections.emptyList())
                                .timestamp(java.time.LocalDateTime.now())
                                .build();

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException exception) {

                List<FieldError> errors = exception
                                .getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> FieldError.builder()
                                                .field(error.getField())
                                                .message(error.getDefaultMessage())
                                                .build())
                                .collect(Collectors.toList());

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .code(ErrorCode.VALIDATION_ERROR.getCode())
                                .message(ErrorCode.VALIDATION_ERROR.getMessage())
                                .errors(errors)
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception exception) {

                return buildResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ErrorCode.INTERNAL_SERVER_ERROR,
                                ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException exception) {

                Throwable cause = exception.getMostSpecificCause();

                if (cause instanceof PSQLException postgresException) {

                        String constraint = postgresException
                                        .getServerErrorMessage()
                                        .getConstraint();

                        if ("uk_user_email".equals(constraint)) {

                                return buildResponse(
                                                HttpStatus.CONFLICT,
                                                ErrorCode.EMAIL_ALREADY_EXISTS,
                                                ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
                        }

                        if ("uk_user_phone_number".equals(constraint)) {

                                return buildResponse(
                                                HttpStatus.CONFLICT,
                                                ErrorCode.PHONE_ALREADY_EXISTS,
                                                ErrorCode.PHONE_ALREADY_EXISTS.getMessage());
                        }
                }

                return buildResponse(
                                HttpStatus.CONFLICT,
                                ErrorCode.RESOURCE_CONFLICT,
                                ErrorCode.RESOURCE_CONFLICT.getMessage());
        }

}