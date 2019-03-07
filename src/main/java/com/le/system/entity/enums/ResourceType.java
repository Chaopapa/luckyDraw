package com.le.system.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @ClassName ResourceType
 * @Author lz
 * @Description 资源类型枚举
 * @Date 2018/10/10 11:22
 * @Version V1.0
 **/
public enum ResourceType implements IEnum<Integer> {
    MENU(0, "菜单"),
    PERMISSION(1, "权限");

    private Integer value;
    private String desc;

    ResourceType(final Integer value, final String desc) {
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
