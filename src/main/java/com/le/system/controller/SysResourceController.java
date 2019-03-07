package com.le.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.le.system.entity.SysResource;
import com.le.system.service.ISysResourceService;
import com.le.core.rest.R;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SysResourceController
 * @Author lz
 * @Description 资源后台管理
 * @Date 2018/10/11 10:06
 * @Version V1.0
 **/
@Slf4j
@Controller
@RequestMapping("/admin/resource")
public class SysResourceController {

    @Autowired
    private ISysResourceService sysResourceService;

    private static final String MANAGE = "admin/resource/manage";

    /**
     * @Description: 跳转资源首页
     * @Title: manage
     * @author lz
     */
    @GetMapping("/manage")
    public String manage(ModelMap model) {
        String permissonTree = sysResourceService.jsonStrTree();
        List<SysResource> list = sysResourceService.findUserTree(null);
        model.put("tree", permissonTree);
        model.put("list", list);
        return MANAGE;
    }

    /**
     * @Description: 获取资源数据
     * @Title: manage
     * @author lz
     */
    @PostMapping("/manageData")
    @ResponseBody
    public R manageData(Long id) {
        try {
            return sysResourceService.manageData(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("获取分页数据失败");
        }
    }

    /**
     * @description 后台资源
     * @author lz
     * @date 2018/10/11 10:14
     * @param sysResource
     * @return com.le.base.util.R
     * @version V1.0.0
     */
    @PostMapping("/editData")
    @ResponseBody
    public R editData(SysResource sysResource) {
        try {
            return sysResourceService.editData(sysResource);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return R.error("操作失败");
    }

    /**
     * @description 获取资源数据树
     * @author lz
     * @date 2018/10/11 10:14
     * @param
     * @return com.le.base.util.R
     * @version V1.0.0
     */
    @GetMapping("/tree")
    @ResponseBody
    public R permissionTree() {
        try {
            return sysResourceService.jsonTree();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("获取树数据失败");
        }
    }

    /**
     * @return boolean 返回类型
     * @Description: 校验权限
     * @author lz
     */
    @GetMapping("/checkPermission")
    @ResponseBody
    public boolean checkcode(String permission, String oPermission) {
        try {
            if (StrUtil.isEmpty(permission)) {
                return false;
            }
            if (permission.equals(oPermission)) {
                return true;
            }
            return sysResourceService.permissionExists(permission);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * @Description: 删除资源
     * @return R 返回类型
     * @author lz
     */
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id){
        try {
            return sysResourceService.del(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("删除失败");
        }
    }
}
