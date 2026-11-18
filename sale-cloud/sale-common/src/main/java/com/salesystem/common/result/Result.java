package com.salesystem.common.result;

import com.salesystem.common.exception.BusinessException;

/**
 * 统一返回体，内部跨服务接口使用。
 * code=200 表示成功；其余为失败。
 */
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    /** 取出数据，非 200 直接抛业务异常，供 Feign 调用方使用 */
    public T unwrap() {
        if (code != 200) {
            throw new BusinessException(code, msg);
        }
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
