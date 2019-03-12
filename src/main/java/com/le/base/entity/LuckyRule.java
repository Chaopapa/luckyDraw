package com.le.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName LuckyRule
 * @Author lz
 * @Description //TODO
 * @Date 2019/3/6 14:53
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("biz_lucky_rule")
public class LuckyRule extends SuperEntity {

    /**
     * 活动名称
     */
    private String name;

    /**
     * 限制订单开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date limitBeginDate;

    /**
     * 限制订单结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date limitEndDate;

    /**
     * 抽奖开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    /**
     * 抽奖结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 限制订单最小金额
     */
    private BigDecimal limitMinPrice;

    /**
     * 限制订单最大金额
     */
    private BigDecimal limitMaxPrice;

    /**
     * 是否限制单号 0 不可重复抽 1 可重复抽
     */
    private Boolean limitNo;

    /**
     * 是否限制手机号 0 不可重复抽 1 可重复抽
     */
    private Boolean limitPhone;

    /**
     * 是否有中奖名单
     */
    private Boolean haveMenu;

    /**
     * 是否有黑名单
     */
    private Boolean haveBlacklist;
}
