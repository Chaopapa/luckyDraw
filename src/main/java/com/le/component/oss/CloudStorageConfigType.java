package com.le.component.oss;

/**
 * @author 严秋旺
 * @since 2019-01-03 17:29
 **/
public enum CloudStorageConfigType {

    /**
     * 阿里云
     */
    ALIYUN(0),
    /**
     * 七牛云
     */
    QINIU(1);

    private int value;

    CloudStorageConfigType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
