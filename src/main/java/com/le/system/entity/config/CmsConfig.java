package com.le.system.entity.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 严秋旺
 * @since 2018-12-06 17:23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CmsConfig extends BaseConfig {
    private static final long serialVersionUID = 6700434628666197473L;
    /**
     * 轮播数量
     */
    private int count = 5;
    /**
     * 间隔时间，毫秒
     */
    private int period = 5000;
    /**
     * 列表是否隐藏轮播内容
     */
    private boolean hideActive = true;
}
