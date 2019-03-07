package com.le.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
     * 限制订单开始时间
     */
    private Date limitBeginDate;

    /**
     * 限制订单结束时间
     */
    private Date limitEndDate;

    /**
     * 抽奖开始时间
     */
    private Date beginDate;

    /**
     * 抽奖结束时间
     */
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
     * 限制单号 0 不可重复抽 1 可重复抽
     */
    private Boolean limitNo;

    /**
     * 限制手机号 0 不可重复抽 1 可重复抽
     */
    private Boolean limitPhone;

}
