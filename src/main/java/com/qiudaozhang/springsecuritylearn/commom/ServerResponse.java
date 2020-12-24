package com.qiudaozhang.springsecuritylearn.commom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author 邱道长
 * 2020/12/24
 * 通用服务端返回对象
 */
@Getter
@Setter
public class ServerResponse <T>{

    private T data;

    private int code;

    private String msg;

    final static int CODE_SUCCESS = 0;
    final static int CODE_FAIL = 1;


    final static String MESSAGE_SUCCESS = "success";


    private ServerResponse(int code , String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> ServerResponse<T> success(){
        return new ServerResponse<>(CODE_SUCCESS,MESSAGE_SUCCESS);
    }

    public static <T> ServerResponse<T> success(T data){
        return new ServerResponse<T>(data,CODE_SUCCESS,MESSAGE_SUCCESS);
    }
    public static <T> ServerResponse<T> error(String msg){
        return new ServerResponse<T>(CODE_FAIL,msg);
    }
}
