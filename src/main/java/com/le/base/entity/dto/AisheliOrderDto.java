package com.le.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName AisheliOrderResponse
 * @Author lz
 * @Description //TODO
 * @Date 2019/3/7 11:28
 * @Version V1.0
 **/
@Data
public class AisheliOrderDto implements Serializable {

    /**
     * 订单号
     */
    private String o;

   /**
    * 订单手机号
    */
   private String p;
   
   /**
    * 客户名称
    */
   private String n;

    /**
     * 客户地址
     */
    private String a;

    /**
     * 金额
     */
    private BigDecimal t;
}
