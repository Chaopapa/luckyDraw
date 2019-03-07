package com.le.system.controller;

import com.le.core.config.shiro.ShiroUtil;
import com.le.system.entity.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ErrorController
 * @Author lz
 * @Description 错误页面跳转
 * @Date 2018/10/15 15:22
 * @Version V1.0
 **/
@Controller
@RequestMapping("/error")
public class ErrorController {

    private static final String COMM_403 = "comm/403";
    private static final String COMM_404 = "comm/404";
    private static final String COMM_500 = "comm/500";

    /**
     * 403页面
     */
    @GetMapping(value = "/403")
    public String error_403() {
        return COMM_403;
    }

    /**
     * 404页面
     */
    @GetMapping(value = "/404")
    public String error_404() {
        return COMM_404;
    }

    /**
     * 500页面
     */
    @GetMapping(value = "/500")
    public String error500() {
        return COMM_500;
    }

    @RequestMapping(value = "/checkLogin")
    @ResponseBody
    public boolean checkLogin(ModelMap model) {
        try {
            SysUser sysUser = ShiroUtil.getUser();
            return sysUser == null ? false : true;
        } catch (Exception e) {
            return false;
        }
    }
}
