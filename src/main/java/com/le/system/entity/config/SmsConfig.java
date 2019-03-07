package com.le.system.entity.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

/**
 * @Author 严秋旺
 * @Date 2018-11-16 16:59
 * @Version V1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SmsConfig extends BaseConfig {
    private static final long serialVersionUID = 24275215138834319L;

    @NotEmpty(message = "accessKeyId不能为空")
    private String accessKeyId;
    @NotEmpty(message = "accessKeySecret不能为空")
    private String accessKeySecret;
    /**
     * 验证码模板id
     */
    @NotEmpty(message = "验证码模板id不能为空")
    private String codeTplCode;
    /**
     * 短信签名
     */
    @NotEmpty(message = "短信签名不能为空")
    private String signName;
    /**
     * 验证码位数
     */
    @Range(min = 1, max = 10, message = "验证码位数只能是{min}-{max}位")
    private int captchaLength = 6;
    /**
     * 有效时间，秒
     */
    @Range(min = 0, max = 9999999, message = "有效时间只能是{min}-{max}，0表示永久有效")
    private int captchaExpires = 1200;
}
