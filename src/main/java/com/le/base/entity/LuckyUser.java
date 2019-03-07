package com.le.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @ClassName User
 * @Author lz
 * @Description 用户表
 * @Date 2018/10/9 11:42
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("biz_lucky_user")
public class LuckyUser extends SuperEntity {

    /**
     * 中奖订单号
     */
    private String no;

    /**
     * 中奖用户名称
     */
    private String name;

    /**
     * 中奖用户手机号
     */
    private String phone;

    /**
     * 中奖用户地址
     */
    private String address;

    /**
     * 规则id
     */
    private Long ruleId;



}

