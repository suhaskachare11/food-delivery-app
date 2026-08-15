package com.quickbite.fooddelivery.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EMAIL_ALREADY_EXISTS(
            "EMAIL_ALREADY_EXISTS",
            "An account with this email already exists."),

    PHONE_ALREADY_EXISTS(
            "PHONE_ALREADY_EXISTS",
            "An account with this phone number already exists."),

    PASSWORD_MISMATCH(
            "PASSWORD_MISMATCH",
            "Password and confirm password do not match."),

    VALIDATION_ERROR(
            "VALIDATION_ERROR",
            "Request validation failed."),

    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred."),

    RESOURCE_CONFLICT(
            "RESOURCE_CONFLICT",
            "The requested resource conflicts with existing data.");

    private final String code;
    private final String message;
}