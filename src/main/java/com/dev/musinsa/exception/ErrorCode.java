package com.dev.musinsa.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    INVALID_INPUT(HttpStatus.BAD_REQUEST, "invalid input."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "invalid format."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "invalid category."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "not found product."),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "not found category."),
    EXTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "external service error.");

    private final HttpStatus code;
    private final String message;

    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}