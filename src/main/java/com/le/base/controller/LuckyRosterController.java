package com.le.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyRoster;
import com.le.base.entity.LuckyRule;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.base.service.ILuckyRosterService;

import com.le.base.service.ILuckyRuleService;
import com.le.core.rest.R;
import com.le.core.util.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @author 陈超
 * @since 2019-03-11
 */
@Slf4j
@Controller
@RequestMapping("/admin/biz/lucky/roster")
public class LuckyRosterController {

    @Autowired
    private ILuckyRosterService luckyRosterService;

    @Autowired
    private ILuckyRuleService luckyRuleService;

    private static final String MENU = "admin/biz/lucky/roster/indexMenu";

    private static final String BLACKLIST = "admin/biz/lucky/roster/indexBlacklist";

    private static final String EDIT = "admin/biz/lucky/roster/edit";

    /**
     * 跳转首页
     *
     * @param model
     * @return
     */
    @RequestMapping({"/index{type}", "/"})
    @RequiresPermissions("base:luckyRoster:view")
    public String index(ModelMap model, @PathVariable("type") RosterTypeEnum type,Long ruleId) {
        List<LuckyRule> ruleList = luckyRuleService.list(null);
        model.put("ruleList",ruleList);

        if(type.equals(RosterTypeEnum.Menu)){
            model.put("type",RosterTypeEnum.Menu);
            return MENU;

        }else{
            model.put("type",RosterTypeEnum.Blacklist);
            model.put("ruleId",ruleId);
            return BLACKLIST;
        }

    }

    /** 获取分页数据
     * @param search 搜索条件
     * @return
     */
    @RequestMapping("/page{type}")
    @ResponseBody
    @RequiresPermissions("base:luckyRoster:view")
    public R page(LuckyRoster search,@PathVariable("type")RosterTypeEnum type,Long rule) {
        Page<LuckyRoster> page = HttpContextUtils.getPage();
        return luckyRosterService.findPage(page, search,type.getValue(),rule);
    }

    /**
     * 跳转信息页
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit{type}")
    @RequiresPermissions("base:luckyRoster:view")
    public String edit(ModelMap model, Long id,@PathVariable("type") RosterTypeEnum type) {
        if (id != null) {
            LuckyRoster luckyRoster = luckyRosterService.getById(id);
            model.put("luckyRoster", luckyRoster);
        }
        List<LuckyRule> ruleList =  luckyRuleService.list(null);
        model.put("ruleList",ruleList);

        if(type.equals(RosterTypeEnum.Menu)){
            model.put("type",RosterTypeEnum.Menu);
        }else{

            model.put("type",RosterTypeEnum.Blacklist);
        }

        return EDIT;
    }

    /**
     * 添加或者更新
     * @param luckyRoster
     * @return
     */
    @RequestMapping("/editData")
    @ResponseBody
    @RequiresPermissions("base:luckyRoster:edit")
    public R editData(@Valid LuckyRoster luckyRoster) {
        return luckyRosterService.editData(luckyRoster);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @RequiresPermissions("base:luckyRoster:edit")
    public R del(@RequestParam("ids") List<Long> ids){
        luckyRosterService.removeByIds(ids);
        return R.success();
    }
}
