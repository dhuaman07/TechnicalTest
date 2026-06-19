package com.dhuaman.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;
    private final String message;
    private final T data;
    private final List<String> errors;

    private ApiResponse(boolean isSuccess, String message, T data, List<String> errors) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, List.of());
    }

    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return new ApiResponse<>(false, message, null, errors);
    }

    @JsonProperty("isSuccess")
    public boolean isSuccess() { return isSuccess; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public List<String> getErrors() { return errors; }
}
