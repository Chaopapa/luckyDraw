package com.le.base.entity.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName ApplyOrderDto
 * @Author lz
 * @Description 申请订单传输
 * @Date 2019/1/9 8:58
 * @Version V1.0
 **/
public class ApplyOrderDto implements Serializable {

    private static final long serialVersionUID = 5300119015556739805L;

    /**
     * 申请人
     */
    @NotBlank(message = "姓名不能为空")
    private String applyName;

    /**
     * 联系人电话
     */
    private String applyPhone;

    /**
     * 单价
     */
    private String code;


}
