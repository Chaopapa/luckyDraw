package com.le.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.le.system.entity.SysUser;
import com.le.system.entity.SysUserRole;

import java.util.List;

/**
 * @ClassName ISysUserService
 * @Author lz
 * @Description 用户角色接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ISysUserRoleService extends IService<SysUserRole> {

    void replaceUserRole(SysUser user, List<Long> roles);
}
