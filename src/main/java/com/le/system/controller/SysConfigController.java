package com.le.system.controller;


import com.le.component.oss.CloudStorageConfig;
import com.le.core.rest.R;
import com.le.system.entity.config.CmsConfig;
import com.le.system.entity.config.SmsConfig;
import com.le.system.service.ISysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 系统配置管理
 *
 * @author 严秋旺
 * @since 2019-1-3 17:27:28
 **/
@Controller
@RequestMapping("/admin/config")
public class SysConfigController {

    @Autowired
    private ISysConfigService configService;


    /**
     * @Description: 跳转配置首页
     * @Title: manage
     * @author lz
     */
    @GetMapping("/oss")
    @RequiresPermissions("sys:config:oss")
    public String oss(ModelMap model) {
        CloudStorageConfig cloudStorageConfig = configService.findConfig(CloudStorageConfig.class);
        model.put("cloudStorageConfigs", cloudStorageConfig);
        return "admin/config/oss";
    }

    /**
     * 保存云存储配置
     *
     * @param cloudStorageConfig
     * @return
     */
    @PostMapping("/editData")
    @ResponseBody
    @RequiresPermissions("sys:config:oss")
    public R oss(@Valid CloudStorageConfig cloudStorageConfig) {
        return configService.saveConfig(cloudStorageConfig);
    }

    /**
     * 短信配置
     *
     * @param model
     * @return
     */
    @GetMapping("/sms")
    @RequiresPermissions("sys:config:sms")
    public String sms(ModelMap model) {
        SmsConfig config = configService.findConfig(SmsConfig.class);
        model.put("config", config);
        return "admin/config/sms";
    }

    @PostMapping("/sms")
    @ResponseBody
    @RequiresPermissions("sys:config:sms")
    public R sms(@Valid SmsConfig smsConfig) {
        return configService.saveConfig(smsConfig);
    }

    /**
     * 广告图配置
     *
     * @param model
     * @return
     */
    @GetMapping("/cms")
    @RequiresPermissions("sys:config:cms")
    public String cms(ModelMap model) {
        CmsConfig cmsConfig = configService.findConfig(CmsConfig.class);
        model.put("cmsConfig", cmsConfig);
        return "admin/config/cms";
    }

    @PostMapping("/cms")
    @ResponseBody
    @RequiresPermissions("sys:config:cms")
    public R cms(@Valid CmsConfig cmsConfig) {
        return configService.saveConfig(cmsConfig);
    }
}
