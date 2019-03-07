package com.le.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * @ClassName SysToken
 * @Author lz
 * @Description Token表
 * @Date 2018/10/9 11:42
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_token")
@Accessors(chain = true)
public class SysToken extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;
}
