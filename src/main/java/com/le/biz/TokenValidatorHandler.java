package com.le.biz;

import com.le.core.exception.TokenException;
import com.le.system.entity.SysToken;

/**
 * @ClassName TokenValidator
 * @Author lz
 * @Description token处理类
 * @Date 2019/1/7 9:49
 * @Version V1.0
 **/
public class TokenValidatorHandler {

    public static Long validateToken() {
        SysToken token = ApiContextHolder.getToken();
        if(null == token){
            throw new TokenException();
        }
        return token.getUserId();
    }
}
