package com.quickbite.fooddelivery.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldError {

    private String field;

    private String message;
    
}
