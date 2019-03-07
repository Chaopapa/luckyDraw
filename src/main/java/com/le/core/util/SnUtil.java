package com.le.core.util;

import com.le.component.sn.SnGenerator;

/**
 * @ClassName SnUtil
 * @Author lz
 * @Description 获取编号
 * @Date 2018/10/18 16:24
 * @Version V1.0
 **/
public class SnUtil {

    private SnUtil(){}

    public static String get(){
        final SnGenerator snGenerator = SnGenerator.INSTANCE;
        return snGenerator.nextId();
    }

    public static String getNo(String prefix){
        String no = get();
        no = no.substring(0,14);
        String r = prefix.concat(no);
        return r;
    }
}
