package app.finplan.handler;

import java.util.Map;

public record ApiResponse<T>(boolean success, T data, Map<String,Object> meta) {
    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(true, data, null);
    }
    public static <T> ApiResponse<T> ok(T data, Map<String,Object> meta){
        return new ApiResponse<>(true, data, meta);
    }
}
