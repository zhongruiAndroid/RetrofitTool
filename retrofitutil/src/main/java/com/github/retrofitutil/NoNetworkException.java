package com.github.retrofitutil;

/**
 * Created by Administrator on 2017/1/10.
 */
public class NoNetworkException extends RuntimeException{
    public NoNetworkException(String message) {
        super(message);
    }
    public NoNetworkException() {
    }
}
