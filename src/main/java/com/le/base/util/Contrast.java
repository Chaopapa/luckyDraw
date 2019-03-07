package com.le.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName Contrast
 * @Author lz
 * @Description //对比两个对象的变化
 * @Date 2019/1/8 10:47
 * @Version V1.0
 **/
public class Contrast {
    private final static Logger logger = LoggerFactory.getLogger(Contrast.class);

    //记录每个修改字段的分隔符
    public static final String separator = ";;;";

    /**
     * @description 比较两个对象,并返回不一致的信息
     * @author lz
     * @date 2019/1/8 10:47
     * @param pojo1 旧值, pojo2 新值
     * @return java.lang.String
     * @version V1.0.0
     */
    public static boolean contrastObj(Object pojo1, Object pojo2) {
        boolean isSameObj = true;
        String str = "";
        try {
            Class clazz = pojo1.getClass();
            Field[] fields = pojo1.getClass().getDeclaredFields();
            int i = 1;
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method method = pd.getReadMethod();
                Object o1 = method.invoke(pojo1);
                Object o2 = method.invoke(pojo2);
                if (o1 == null || o2 == null) {
                    continue;
                }
                if (o1 instanceof Date) {
                    continue;
                }
                if (!o1.toString().equals(o2.toString())) {
                    if (i != 1) {
                        str += separator;
                    }
                    str += "字段名称" + field.getName() + ",旧值:" + o1 + ",新值:" + o2;
                    i++;
                    isSameObj = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("{}", str);
        return isSameObj;
    }

    public static void main(String [] args){

    }
}
