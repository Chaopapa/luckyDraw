package com.le.base.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RegisterDto
 * @Author lz
 * @Description 注册传输对象
 * @Date 2018/12/21 9:24
 * @Version V1.0
 **/
@Data
public class RegisterDto implements Serializable {

    /**
     * 小程序code
     */
    private String wxCode;

    /**
     * 验证码
     */
    private String code;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String headImg;

    /**
     * 用户名称
     */
    private String name;
}
