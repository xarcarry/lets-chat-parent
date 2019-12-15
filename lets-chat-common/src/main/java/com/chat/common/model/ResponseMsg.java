package com.chat.common.model;

import lombok.Data;

@Data
public class ResponseMsg<T>{
    private int code;
    private T msg;

    public ResponseMsg(int code, T msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMsg() {
    }
}
