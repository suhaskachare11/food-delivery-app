package com.quickbite.fooddelivery.common.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    
    private final ErrorCode errorCode;

    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
        this.errorCode = ErrorCode.EMAIL_ALREADY_EXISTS;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}