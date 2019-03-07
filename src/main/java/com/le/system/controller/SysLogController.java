package com.le.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.system.entity.SysLog;
import com.le.system.service.ISysLogService;
import com.le.core.rest.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SysLogController
 * @Author lz
 * @Description 系统日志管理
 * @Date 2018/10/11 10:06
 * @Version V1.0
 **/
@Controller
@RequestMapping("/admin/sysLog")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    private static final String MANAGE = "admin/log/manage";
    /**
     * @Description: 跳转日志首页
     * @Title: manage
     * @author wxy
     */
    @GetMapping("/manage")
    public String manage() {
        return MANAGE;
    }

    /**
     * @Description: 跳转日志分页
     * @Title: manage
     * @author wxy
     */
    @PostMapping("/manageData")
    @ResponseBody
    public R manageData(Page<SysLog> page) {
        return sysLogService.manageData(page);
    }

}
