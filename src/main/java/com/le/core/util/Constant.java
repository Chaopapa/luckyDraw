package com.le.core.util;

/**
 * @ClassName Constant
 * @Author lz
 * @Description 静态常量
 * @Date 2018/10/12 12:08
 * @Version V1.0
 **/
public final class Constant {

    /**
     * 超级用户id
     */
    public static final Long SUPER_ADMIN = 1L;

    /**
     * 树根节点id
     */
    public static final String TREE_ROOT_ID = "0";


    /**
     * 小程序openid
     */
    public static final String OPEN_ID = "openid";

    /**
     *  token Redis Key
     */
    public static final String REDIS_TOKEN_KEY = "TOKEN:KEY:";

    /**
     * 前缀
     */
    public static final String PREFIX = "longe";

    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_OSS_CONFIG_KEY = "CLOUD_STORAGE_OSS_CONFIG_KEY";

    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_SMS_CONFIG_KEY = "CLOUD_STORAGE_SMS_CONFIG_KEY";

    /**
     * 云服务商
     */
    public enum Cloud {

        /**
         * 阿里云
         */
        ALIYUN(0),
        /**
         * 七牛云
         */
        QINIU(1);

        private int value;

        Cloud(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum SMS {

        /**
         * 大于
         */
        DAYU(0),
        /**
         * 容联云
         */
        RONGLIAN(1);

        private int value;

        SMS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
