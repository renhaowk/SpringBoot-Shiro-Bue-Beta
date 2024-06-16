package com.haoren.springbootshirovuebeta.config.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){
        super("用户无此接口权限");
    }
}
