package com.le.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.system.entity.SysRole;
import com.le.system.service.ISysResourceService;
import com.le.system.service.ISysRoleService;
import com.le.core.rest.R;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SysRoleController
 * @Author lz
 * @Description 角色后台管理
 * @Date 2018/10/11 10:06
 * @Version V1.0
 **/
@Slf4j
@Controller
@RequestMapping("/admin/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysResourceService sysResourceService;

    private static final String MANAGE = "admin/role/manage";

    private static final String EDIT = "admin/role/edit";

    /**
     * @Description: 跳转角色首页
     * @Title: manage
     * @author lz
     */
    @GetMapping("/manage")
    public String manage() {
        return MANAGE;
    }

    /**
     * @Description: 获取角色分页数据
     * @Title: manage
     * @author lz
     */
    @PostMapping("/manageData")
    @ResponseBody
    public R manageData(Page<SysRole> page, String name) {
        try {
            return sysRoleService.manageData(page, name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("获取分页数据失败");
        }
    }

    /**
     * @Description: 跳转角色信息页
     * @Title: edit
     * @author lz
     */
    @GetMapping("/edit")
    public String edit(ModelMap model, Long id) {
        if (id != null) {
            SysRole role = sysRoleService.getById(id);
            model.put("role", role);
            String resources = sysResourceService.queryJsonTreeByRoleId(id);
            log.info(resources);
            model.put("resources", resources);
        }
        return EDIT;
    }

    /**
     * @Description: 添加或者更新角色
     * @Title: editData
     * @author lz
     */
    @PostMapping("/editData")
    @ResponseBody
    public R editData(SysRole role, Long[] resourceIds) {
        try {
            return sysRoleService.editData(role, resourceIds);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return R.error("操作失败");
    }

    /**
     * @Description: 删除角色
     * @return R 返回类型
     * @author lz
     */
    @GetMapping("/del")
    @ResponseBody
    public R del(@RequestParam("ids") List<Long> ids){
        try {
            return sysRoleService.del(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("删除失败");
        }
    }
}
