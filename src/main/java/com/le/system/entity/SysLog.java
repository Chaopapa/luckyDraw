package com.le.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @ClassName SysLog
 * @Author lz
 * @Description 日志表
 * @Date 2018/10/9 11:42
 * @Version V1.0
 **/
@Data
@TableName("sys_log")
@EqualsAndHashCode(callSuper = false)
public class SysLog extends SuperEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户名
     */
    private String operation;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 执行时长(毫秒)
     */
    private Long time;
    /**
     * IP地址
     */
    private String ip;

}
