package com.le.core.exception;

/**
 * @ClassName TokenException
 * @Author lz
 * @Description token 异常
 * @Date 2018/10/30 11:01
 * @Version V1.0
 **/
public class TokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String token;

    public TokenException() {
        super();
    }

    public TokenException(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

