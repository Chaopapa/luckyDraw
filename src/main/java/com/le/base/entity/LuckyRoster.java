package com.le.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("biz_lucky_roster")
public class LuckyRoster extends SuperEntity {

    /**
     * 订单号
     */
    private String no;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 订单类型
     */
    private RosterTypeEnum type;

    /**
     * 抽奖规则
     */
    private Long ruleId;

    /**
     * 抽奖规则名称
     */
    @TableField(exist = false)
    private  String ruleName;





}
