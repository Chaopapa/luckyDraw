package com.le.base.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum RosterTypeEnum implements IEnum<Integer> {
    Menu(0, "抽奖名单"), Blacklist(1, "黑名单");

    private Integer value;
    private String desc;

    RosterTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
