package com.le.core.exception;

/**
 * @author 严秋旺
 * @since 2019-01-02 17:37
 **/
public class TokenTimeoutException extends TokenException {

    public TokenTimeoutException(String token) {
        super(token);
    }
}
