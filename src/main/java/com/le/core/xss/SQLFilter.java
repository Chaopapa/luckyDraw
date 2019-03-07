package com.le.core.xss;

import com.le.core.exception.LEException;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName SQLFilter
 * @Author lz
 * @Description SQLFilter 注入过滤配置類
 * @Date 2018/10/8 13:50
 * @Version V1.0
 **/
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new LEException("包含非法字符");
            }
        }

        return str;
    }
}
