package com.quickbite.fooddelivery.common.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private boolean success;

    private String code;

    private String message;

    private List<FieldError> errors;

    private LocalDateTime timestamp;
    
}
