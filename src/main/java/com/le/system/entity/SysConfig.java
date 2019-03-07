package com.le.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName SysConfig
 * @Author lz
 * @Description 配置表
 * @Date 2018/10/16 16:57
 * @Version V1.0
 **/
@Data
@TableName("sys_config")
@EqualsAndHashCode(callSuper = false)
public class SysConfig extends SuperEntity {

    private static final long serialVersionUID = 9075598764740618776L;
    /**
     * 参数名
     */
    @NotBlank(message = "参数名不能为空")
    private String paramKey;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 备注
     */
    private String remark;
}
