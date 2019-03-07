package com.le.component.sms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.component.sms.entity.ComSmslog;
import com.le.component.sms.service.IComSmslogService;
import com.le.core.rest.R;
import com.le.core.util.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 短信记录 前端控制器
 * </p>
 *
 * @author 严秋旺
 * @since 2018-11-19
 */
@Slf4j
@Controller
@RequestMapping("/admin/component/smslog")
public class ComSmslogController {
    @Autowired
    private IComSmslogService comSmslogService;

    private static final String INDEX = "admin/component/smslog/index";

    /**
     * @Description: 跳转短信记录首页
     * @Title: manage
     * @author lz
     */
    @RequestMapping({"/index", "/"})
    @RequiresPermissions("component:smslog:view")
    public String index(ModelMap model) {
        return INDEX;
    }

    /**
     * @Description: 获取短信记录分页数据
     * @Title: manage
     * @author lz
     */
    @RequestMapping("/page")
    @ResponseBody
    @RequiresPermissions("component:smslog:view")
    public R page(ComSmslog search) {
        try {
            Page<ComSmslog> page = HttpContextUtils.getPage();
            return comSmslogService.findPage(page, search);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error("获取分页数据失败");
        }
    }

}
