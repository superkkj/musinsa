package com.dev.musinsa.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(
        int resultCode,
        String resultMsg,
        T data,
        List<String> errors

) {
    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null, null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data, null);
    }

    public static <T> CommonResponse<T> fail(ErrorResponse errorResponse) {
        return new CommonResponse<>(errorResponse.errorCode().value(), errorResponse.errorMessage(), null, null);
    }

    public static <T> CommonResponse<T> fail(ErrorResponse errorResponse, List<String> error) {
        return new CommonResponse<>(errorResponse.errorCode().value(), errorResponse.errorMessage(), null, error);
    }
}
