package com.dev.musinsa.exception.advice;

import com.dev.musinsa.controller.response.CommonResponse;
import com.dev.musinsa.controller.response.ErrorResponse;
import com.dev.musinsa.exception.ErrorCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice("com.dev.musinsa")
public class CommonControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> handleInvalidFormatException(InvalidFormatException ex) {
        log.error("InvalidFormatException: ", ex);

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT);
        return ResponseEntity
                .status(errorResponse.errorCode())
                .body(CommonResponse.fail(errorResponse, List.of(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT);
        return ResponseEntity
                .status(errorResponse.errorCode())
                .body(CommonResponse.fail(errorResponse, errors));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonResponse<Void>> handleCommonException(CommonException ex) {
        log.error("Handling CommonException: ", ex);

        ErrorResponse errorResponse = ErrorResponse.of(ex.getError());
        return ResponseEntity
                .status(errorResponse.errorCode())
                .body(CommonResponse.fail(errorResponse, List.of(ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.EXTERNAL_SERVICE_ERROR);
        return ResponseEntity
                .status(errorResponse.errorCode())
                .body(CommonResponse.fail(errorResponse));
    }
}