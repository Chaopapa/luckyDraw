package com.le.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName LoginDto
 * @Author lz
 * @Description 登录传输对象
 * @Date 2018/12/21 9:24
 * @Version V1.0
 **/
@Data
public class LoginDto implements Serializable {

    private static final long serialVersionUID = -958468220638471885L;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

}
