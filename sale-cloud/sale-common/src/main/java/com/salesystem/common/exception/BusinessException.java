package com.salesystem.common.exception;

/**
 * 业务异常，由各服务在业务校验失败时抛出，统一异常处理器会转成 Result 返回。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
