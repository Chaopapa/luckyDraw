package com.le.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName SysOss
 * @Author lz
 * @Description 图片表
 * @Date 2018/10/16 16:58
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_oss")
@EqualsAndHashCode(callSuper = false)
public class SysOss extends SuperEntity {

    private static final long serialVersionUID = 3115225924265271513L;
    /**
     * URL地址
     */
    private String url;

}
