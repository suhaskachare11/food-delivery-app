package com.quickbite.fooddelivery.common.exception;

public class PasswordMismatchException extends RuntimeException {
   
    private final ErrorCode errorCode;

    public PasswordMismatchException() {
        super(ErrorCode.PASSWORD_MISMATCH.getMessage());
        this.errorCode = ErrorCode.PASSWORD_MISMATCH;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
