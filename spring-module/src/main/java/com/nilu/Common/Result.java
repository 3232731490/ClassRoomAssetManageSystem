package com.nilu.Common;

import org.springframework.context.annotation.Configuration;

/**
 * @author: 倪路
 * Time: 2021/9/25-9:14
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@Configuration
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public static com.nilu.Common.Result success() {
        com.nilu.Common.Result result = new com.nilu.Common.Result<>();
        result.setCode("0");
        result.setMsg("成功");
        return result;
    }

    public static <T> com.nilu.Common.Result<T> success(T data) {
        com.nilu.Common.Result<T> result = new com.nilu.Common.Result<>(data);
        result.setCode("0");
        result.setMsg("成功");
        return result;
    }

    public static com.nilu.Common.Result error(String code, String msg) {
        com.nilu.Common.Result result = new com.nilu.Common.Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}