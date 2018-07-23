package com.github.retrofitutil;

import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2017/1/10.
 */
public class ErrorMsg {
    public ErrorMsg() {
    }
    public static String error(Throwable throwable) {
        throwable.printStackTrace();
        String message;
        if (throwable instanceof NoNetworkException) {
            message = throwable.getMessage();
        } else if(throwable instanceof SocketTimeoutException){
            message = "网络连接超时,请稍后重试!";
        }else {
            message = "连接服务器失败";
        }
        return message;
    }
}
