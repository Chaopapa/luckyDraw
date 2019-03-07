package com.le.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.le.core.rest.R;
import com.le.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName ISysUserService
 * @Author lz
 * @Description 用户接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ISysUserService extends IService<SysUser> {

    /**
     * @param request 请求
     * @return com.le.base.util.R
     * @description 登录校验
     * @author lz
     * @date 2018/10/11 10:36
     * @version V1.0.0
     */
    R validate(HttpServletRequest request, String username, String password, boolean rememberMe);

    /**
     * @param username 账号
     * @return com.le.admin.entity.SysUser
     * @description 通过账号查找后台登良者
     * @author lz
     * @date 2018/10/11 10:39
     * @version V1.0.0
     */
    SysUser findByUsername(String username);

    /**
     * @param roles 角色id user
     * @return com.le.base.util.R
     * @description 添加或修改用户
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    R editData(SysUser user, List<Long> roles);

    /**
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @description rbac 查找用户所有权限permission
     * @author lz
     * @date 2018/10/11 10:44
     * @version V1.0.0
     */
    List<String> findAuthorities(Long userId);

    /**
     * @param username 用户账号
     * @return java.util.List<java.lang.String>
     * @description rbac 查找用户账号是否存在
     * @author lz
     * @date 2018/10/11 10:44
     * @version V1.0.0
     */
    boolean usernameExists(String username);

    /**
     * @param ids
     * @return com.le.base.util.R
     * @description 删除用户
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    R del(List<Long> ids);

    /**
     * @param id
     * @return com.le.base.util.R
     * @description 重置密码 123456
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    R resetPassword(Long id);

    /**
     * @Description:用户管理分页
     * @Title:
     * @author
     */
    R findPage(Page<SysUser> page, SysUser search);
}
