package com.le.base.controller;

import com.le.base.service.ILuckyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/admin/biz/lucky/rule")
public class LuckyRuleController {

    @Autowired
    private ILuckyUserService luckyUserService;

    private static final String INDEX = "biz/user/index";

    private static final String ADD = "biz/user/add";

    private static final String AUTH = "biz/user/auth";

    private static final String EDIT_AUTH = "biz/user/editAuth";

    private static final String BLACKLIST = "biz/user/blacklist";


}
