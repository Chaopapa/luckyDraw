package com.le.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.le.core.rest.R;
import com.le.system.entity.SysRole;
import com.le.system.entity.SysUserRole;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ISysUserService
 * @Author lz
 * @Description 角色接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ISysRoleService extends IService<SysRole> {

    /**
     * @param userId 用户id
     * @return java.lang.String
     * @description 通过userId 查找角色
     * @author lz
     * @date 2018/10/10 10:59
     * @version V1.0.0
     */
    Map<String, SysUserRole> findUserRoleMap(Long userId);

    /**
     * @param page, name 角色名
     * @return com.le.base.util.R
     * @description 后台角色分页
     * @author lz
     * @date 2018/10/11 10:12
     * @version V1.0.0
     */
    R manageData(Page<SysRole> page, String name);

    /**
     * @param role
     * @return com.le.base.util.R
     * @description 添加或修改角色
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    R editData(SysRole role, Long[] resourceIds);

    /**
     * @param ids
     * @return com.le.base.util.R
     * @description 删除角色
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    R del(List<Long> ids);

    /**
     * 查询用户角色列表
     *
     * @param userId 用户ID
     */
    List<SysRole> findUserRole(long userId);
}
