package com.le.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.le.core.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName SysRole
 * @Author lz
 * @Description 角色表
 * @Date 2018/9/30 9:00
 * @Version V1.0
 **/
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends SuperEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 角色名
     */
    private String name;

    /**
     * 角色字符串
     */
    private String role;

    @TableField(exist = false)
    private Set<SysResource> roles = new HashSet<>();

}
