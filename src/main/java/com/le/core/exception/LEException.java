package com.le.core.exception;

/**
 * @ClassName LEException
 * @Author lz
 * @Description 自定义异常
 * @Date 2018/10/9 14:51
 * @Version V1.0
 **/
public class LEException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public LEException(String msg) {
        super(msg);
    }

    public LEException(String msg, Throwable e) {
        super(msg, e);
    }

}
