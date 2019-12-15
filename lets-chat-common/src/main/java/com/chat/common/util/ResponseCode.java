package com.chat.common.util;

/**
 * 系统返回的状态码
 * @author xar
 */
public enum ResponseCode {
    /**
     * 代表当前请求为短时间内重复的请求
     */
    REPEAT_REQUEST(600, "重复的请求");

    private int code;
    private String msg;

    private ResponseCode(int code, String msg){
        this.code = code;
        this.msg = msg;
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
}
