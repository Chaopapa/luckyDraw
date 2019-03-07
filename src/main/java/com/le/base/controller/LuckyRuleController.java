package com.le.base.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyRule;
import com.le.base.service.ILuckyRuleService;
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
@RequestMapping("/admin/biz/lucky/rule")
public class LuckyRuleController {
    @Autowired
    private ILuckyRuleService luckyRuleService;

    private static final String INDEX = "admin/biz/lucky/rule/index";

    private static final String EDIT = "admin/biz/lucky/rule/edit";

    /**
     * 跳转首页
     *
     * @param model
     * @return
     */
    @RequestMapping({"/index", "/"})
    @RequiresPermissions("base:bizLuckyRule:view")
    public String index(ModelMap model) {
        return INDEX;
    }

    /** 获取分页数据
     * @param search 搜索条件
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    @RequiresPermissions("base:bizLuckyRule:view")
    public R page(LuckyRule search) {
        Page<LuckyRule> page = HttpContextUtils.getPage();
        return luckyRuleService.findPage(page, search);
    }

    /**
     * 跳转信息页
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit")
    @RequiresPermissions("base:bizLuckyRule:view")
    public String edit(ModelMap model, Long id) {
        if (id != null) {
            LuckyRule luckyRule = luckyRuleService.getById(id);
            model.put("luckyRule", luckyRule);
        }
        return EDIT;
    }

    /**
     * 添加或者更新
     * @param luckyRule
     * @return
     */
    @RequestMapping("/editData")
    @ResponseBody
    @RequiresPermissions("base:bizLuckyRule:edit")
    public R editData(@Valid LuckyRule luckyRule) {
        return luckyRuleService.editData(luckyRule);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @RequiresPermissions("base:bizLuckyRule:edit")
    public R del(@RequestParam("ids") List<Long> ids){
        luckyRuleService.removeByIds(ids);
        return R.success();
    }
}
