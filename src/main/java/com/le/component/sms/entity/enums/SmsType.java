package com.le.component.sms.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @Author 严秋旺
 * @Date 2018-11-19 14:05
 * @Version V1.0
 **/
public enum SmsType implements IEnum<Integer> {
    REGISTER(0, "注册"),LOGIN(1, "登录"),updatePassWord(2,"修改密码");
    private int value = 1;
    private String desc = "";

    SmsType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
