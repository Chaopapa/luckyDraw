package com.le.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.core.rest.R;
import com.le.core.util.HttpContextUtils;
import com.le.system.entity.SysUser;
import com.le.system.entity.SysUserRole;
import com.le.system.service.ISysRoleService;
import com.le.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @ClassName UserController
 * @Author lz
 * @Description 用户后台管理
 * @Date 2018/9/30 9:20
 * @Version V1.0
 **/
@Slf4j
@Controller
@RequestMapping("/admin/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    private static final String MANAGE = "admin/user/manage";

    private static final String EDIT = "admin/user/edit";

    /**
     * @Description: 跳转用户首页
     * @Title: manage
     * @author lz
     */
    @GetMapping("/manage")
    @RequiresPermissions("sys:user:view")
    public String manage(ModelMap model) {
        return MANAGE;
    }

    /**
     * @Description: 获取用户分页数据
     * @Title: manage
     * @author lz
     */
    @PostMapping("/manageData")
    @ResponseBody
    @RequiresPermissions("sys:user:view")
    public R manageData(SysUser search) {
        try {
            Page<SysUser> page = HttpContextUtils.getPage();
            return sysUserService.findPage(page, search);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("获取分页数据失败");
        }
    }

    /**
     * @Description: 跳转用户信息页
     * @Title: edit
     * @author lz
     */
    @GetMapping("/edit")
    @RequiresPermissions("sys:user:view")
    public String edit(ModelMap model, Long id) {
        if (id != null) {
            SysUser user = sysUserService.getById(id);
            model.put("user", user);

            Map<String, SysUserRole> roleMap = sysRoleService.findUserRoleMap(id);
            model.put("roleMap", roleMap);
        }

        model.put("roles", sysRoleService.list(null));
        return EDIT;
    }

    /**
     * @Description: 添加或者更新用户
     * @Title: editData
     * @author lz
     */
    @PostMapping("/editData")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    public R editData(SysUser user, @RequestParam("roleId") List<Long> roles) {
        try {
            return sysUserService.editData(user, roles);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return R.error("操作失败");
        }
    }

    /**
     * @Description: 重置密码 123456
     * @Title: resetPassword
     * @author lz
     */
    @RequestMapping(value = "/reset")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    public R reset(Long id) {
        try {
            return sysUserService.resetPassword(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("操作失败");
        }
    }

    /**
     * @Description: 检查用户名
     * @Title: checkUserName
     * @author lz
     */
    @PostMapping(value = "/checkUserName")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    public boolean checkUserName(String oldUsername, String username) {
        if (StrUtil.isNotBlank(oldUsername) && oldUsername.equals(username)) {
            return false;
        }
        return sysUserService.usernameExists(username);
    }

    /**
     * @return R 返回类型
     * @Description: 删除用户
     * @author lz
     */
    @GetMapping("/del")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    public R del(@RequestParam("ids") List<Long> ids) {
        try {
            return sysUserService.del(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("删除失败");
        }
    }
}
