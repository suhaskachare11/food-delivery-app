package com.quickbite.fooddelivery.common.exception;

public class PhoneAlreadyExistsException extends RuntimeException {
  
    private final ErrorCode errorCode;

    public PhoneAlreadyExistsException() {
        super(ErrorCode.PHONE_ALREADY_EXISTS.getMessage());
        this.errorCode = ErrorCode.PHONE_ALREADY_EXISTS;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
