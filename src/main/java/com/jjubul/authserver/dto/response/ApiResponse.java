package com.jjubul.authserver.dto.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public ApiResponse(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    } // 추후 제거하고 밑의 private 생성자로 대체할 예정입니다. 참고하여 수정 바랍니다.

    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, "SUCCESS", message, data);
    }

    public static <T> ApiResponse<T> failure(String message, T data) {
        return new ApiResponse<>(false, "SERVER_ERROR", message, data);
    }

    public static <T> ApiResponse<T> failure(String message, String code, T data) {
        return new ApiResponse<>(false, code, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, "SUCCESS", message, null);
    }
}
