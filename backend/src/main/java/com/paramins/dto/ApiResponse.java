package com.paramins.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success = success; this.message = message;
        this.data = data; this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(true, msg, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(false, msg, null, LocalDateTime.now());
    }

    public boolean isSuccess()              { return success; }
    public void setSuccess(boolean v)       { this.success = v; }
    public String getMessage()              { return message; }
    public void setMessage(String v)        { this.message = v; }
    public T getData()                      { return data; }
    public void setData(T v)                { this.data = v; }
    public LocalDateTime getTimestamp()     { return timestamp; }
    public void setTimestamp(LocalDateTime v){ this.timestamp = v; }
}
