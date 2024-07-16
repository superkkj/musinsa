package com.dev.musinsa.exception.advice;


import com.dev.musinsa.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final ErrorCode error;

    public CommonException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }
}