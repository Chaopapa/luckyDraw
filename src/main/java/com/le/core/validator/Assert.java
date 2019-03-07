package com.le.core.validator;

import cn.hutool.core.util.StrUtil;
import com.le.core.exception.LEException;

/**
 * @ClassName Assert
 * @Author lz
 * @Description 断言
 * @Date 2018/10/16 11:16
 * @Version V1.0
 **/
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new LEException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new LEException(message);
        }
    }
}
