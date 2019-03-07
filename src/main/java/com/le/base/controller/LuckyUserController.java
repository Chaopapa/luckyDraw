package com.le.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyUser;
import com.le.base.service.ILuckyUserService;
import com.le.core.rest.R;
import com.le.core.util.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2019-03-07
 */
@Slf4j
@Controller
@RequestMapping("/admin/biz/lucky/user")
public class LuckyUserController {
    @Autowired
    private ILuckyUserService luckyUserService;

    private static final String INDEX = "admin/biz/lucky/user/index";

    private static final String EDIT = "admin/biz/lucky/user/edit";

    /**
     * 跳转首页
     *
     * @param model
     * @return
     */
    @RequestMapping({"/index", "/"})
    @RequiresPermissions("base:bizLuckyUser:view")
    public String index(ModelMap model) {
        return INDEX;
    }

    /** 获取分页数据
     * @param search 搜索条件
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    @RequiresPermissions("base:bizLuckyUser:view")
    public R page(LuckyUser search) {
        Page<LuckyUser> page = HttpContextUtils.getPage();
        return luckyUserService.findPage(page, search);
    }

    /**
     * 跳转信息页
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit")
    @RequiresPermissions("base:bizLuckyUser:view")
    public String edit(ModelMap model, Long id) {
        if (id != null) {
            LuckyUser luckyUser = luckyUserService.getById(id);
            model.put("luckyUser", luckyUser);
        }
        return EDIT;
    }

    /**
     * 添加或者更新
     * @param luckyUser
     * @return
     */
    @RequestMapping("/editData")
    @ResponseBody
    @RequiresPermissions("base:bizLuckyUser:edit")
    public R editData(@Valid LuckyUser luckyUser) {
        return luckyUserService.editData(luckyUser);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @RequiresPermissions("base:bizLuckyUser:edit")
    public R del(@RequestParam("ids") List<Long> ids){
        luckyUserService.removeByIds(ids);
        return R.success();
    }
}
