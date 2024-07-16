package com.dev.musinsa.controller.response;

import com.dev.musinsa.exception.ErrorCode;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(
        HttpStatus errorCode,
        String errorMessage
) {
    public static ErrorResponse of(ErrorCode error) {
        return ErrorResponse.builder()
                .errorCode(error.getCode())
                .errorMessage(error.getMessage())
                .build();
    }
}