package com.salesystem.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> handleRuntimeException(RuntimeException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 500);
        res.put("msg", e.getMessage());
        return res;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 500);
        res.put("msg", "服务器内部错误");
        return res;
    }
}
