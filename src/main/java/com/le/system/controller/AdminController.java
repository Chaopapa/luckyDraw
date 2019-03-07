package com.le.system.controller;

import com.le.core.config.shiro.ShiroUtil;
import com.le.core.rest.R;
import com.le.system.entity.SysResource;
import com.le.system.entity.SysUser;
import com.le.system.service.ISysResourceService;
import com.le.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.DisabledAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ClassName AdminController
 * @Author lz
 * @Description 系统
 * @Date 2018/10/9 15:30
 * @Version V1.0
 **/
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysResourceService sysResourceService;

    private static final String INDEX = "admin/index";

    private static final String MAIN = "admin/main";

    private static final String LOGIN = "admin/login";

    private static final String REDIRECT = "redirect:/";

    /**
     * 登录页面跳转
     */
    @RequestMapping("/login")
    public String login() {
        return LOGIN;
    }

    /**
     * 首页
     */
    @RequestMapping("/index")
    public String index() {
        return INDEX;
    }

    /**
     * 首页
     */
    @RequestMapping({"/main", "/"})
    public String main(ModelMap model) {
        SysUser user = ShiroUtil.getUser();
        model.addAttribute("name", user.getName());
        List<SysResource> list = sysResourceService.findUserTree(user);
        model.put("perList", list);
        return MAIN;
    }

    /**
     * 登录校验
     */
    @RequestMapping("/loginCheck")
    @ResponseBody
    public R validate(HttpServletRequest request, String username, String password, @RequestParam(required = false, defaultValue = "false") Boolean rememberMe) {
        try {
            return sysUserService.validate(request, username, password, rememberMe);
        } catch (DisabledAccountException e) {
            return R.error(e.getMessage());
        } catch (AccountException e) {
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("登录出错", e);
            return R.error("登录出错");
        }
    }

    /**
     * 退出登录
     */
    @RequestMapping("/logout")
    public String logout() {
        ShiroUtil.logout();
        return REDIRECT + LOGIN;
    }
}
