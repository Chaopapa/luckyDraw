package com.le.component.sms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.le.component.sms.entity.enums.SmsType;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 短信记录
 * </p>
 *
 * @author 严秋旺
 * @since 2018-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_sms_log")
public class ComSmslog extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 短信类型
     */
    private SmsType type;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 标记
     */
    private Integer flag;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板id
     */
    private String tplCode;

    /**
     * 发送结果
     */
    private String result;


}
