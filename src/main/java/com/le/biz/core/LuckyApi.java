package com.le.biz.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.le.base.entity.LuckyRoster;
import com.le.base.entity.LuckyRule;
import com.le.base.entity.LuckyUser;
import com.le.base.entity.dto.AisheliOrderDto;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.base.service.ILuckyRosterService;
import com.le.base.service.ILuckyRuleService;
import com.le.base.service.ILuckyUserService;
import com.le.biz.TokenValidatorHandler;
import com.le.core.properties.LongeProperties;
import com.le.core.rest.R;
import com.le.core.rest.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName LuckOrderApi
 * @Author lz
 * @Description //TODO
 * @Date 2019/3/6 16:49
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/templates/admin/biz/lucky")
public class LuckyApi {

    @Autowired
    private ILuckyRuleService luckyRuleService;

    @Autowired
    private ILuckyUserService luckyUserService;

    @Autowired
    private ILuckyRosterService luckyRosterService;

    @Autowired
    private LongeProperties longeProperties;

    @GetMapping("get")
    public R getLuckyOne() {
        Date date = new Date();
        long interfaceTime = date.getTime();
        LuckyRule rule = luckyRuleService.getByTime();
        if (null == rule) {
            return R.error("请先设置活动规格");
        }
        Long ruleId = rule.getId();
        Map<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put("beginDate", rule.getLimitBeginDate());
        paramMap.put("endDate", rule.getLimitEndDate());
        if (rule.getLimitMinPrice() != null && rule.getLimitMaxPrice() != null) {
            paramMap.put("limitMinPrice", rule.getLimitMinPrice().toString());
            paramMap.put("limitMaxPrice", rule.getLimitMaxPrice().toString());
        }

        List<String> nos = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        if(!(Boolean.TRUE.equals(rule.getLimitNo()) && Boolean.TRUE.equals(rule.getLimitNo()))){
            List<LuckyUser> luckyUsers = luckyUserService.getByRuleId(ruleId);
            if(CollectionUtil.isNotEmpty(luckyUsers)){
                luckyUsers.stream().forEach(luckyUser -> {
                    if(Boolean.FALSE.equals(rule.getLimitNo())){
                        nos.add(luckyUser.getNo());
                    }
                    if(Boolean.FALSE.equals(rule.getLimitPhone())){
                        phones.add(luckyUser.getPhone());
                    }
                });
            }
        }

        List<AisheliOrderDto> orders = new ArrayList<>();
        String no = null;

        List<LuckyRoster> rosters = null;
        if(Boolean.TRUE.equals(rule.getHaveMenu()) && Boolean.TRUE.equals(rule.getHaveBlacklist())){
            rosters = luckyRosterService.findByRuleId(ruleId, null);
        }else if(Boolean.TRUE.equals(rule.getHaveMenu())){
            rosters = luckyRosterService.findByRuleId(ruleId, RosterTypeEnum.Menu);
        }else if(Boolean.TRUE.equals(rule.getHaveBlacklist())){
            rosters = luckyRosterService.findByRuleId(ruleId, RosterTypeEnum.Blacklist);
        }

        if(CollectionUtil.isNotEmpty(rosters)){
            for (LuckyRoster roster : rosters) {
                if(RosterTypeEnum.Menu.equals(roster.getType())) {
                    AisheliOrderDto dto = new AisheliOrderDto();
                    BeanUtil.copyProperties(roster, dto);
                    orders.add(dto);
                }else if(RosterTypeEnum.Blacklist.equals(roster.getType())){
                    phones.add(roster.getPhone());
                }
            }

            if(CollectionUtil.isNotEmpty(orders)){
                for (AisheliOrderDto order : orders) {
                    if(nos.contains(order.getO())){
                        continue;
                    }
                    return new R().putData("no", order.getO());
                }
            }
        }

        if(CollectionUtil.isNotEmpty(nos)){
            paramMap.put("luckyNoArr", nos);
        }

        if(CollectionUtil.isNotEmpty(phones)){
            paramMap.put("luckyPhoneArr", phones);
        }

        Long beginTime = new Date().getTime();
        String orderStr = HttpUtil.post(longeProperties.getOrderUrl(), paramMap);

        Long totalConsumedTime = new Date().getTime() - beginTime;
        log.info("订单接口耗时：" + totalConsumedTime);
        if (StrUtil.isNotBlank(orderStr)) {
            R r = JSON.parseObject(orderStr, R.class);
            if (RCode.success.getValue().equals(r.getRespCode())) {
                Map<String, Object> data = r.getData();
                String order = MapUtil.getStr(data, "orders");
                orders = JSON.parseArray(order, AisheliOrderDto.class);
            }else if("0001".equals(r.getRespCode())){
                return R.error("缺少抽奖时间参数");
            }else if(RCode.empty.getValue().equals(r.getRespCode())){
                return R.error("无单号可抽奖");
            }
        }
        if(CollectionUtil.isNotEmpty(orders)){
            no = luckyUserService.doDrawAndSaveUser(orders, ruleId);
        }

        if(StrUtil.isNotBlank(no)){
            Long lastTime = new Date().getTime() - interfaceTime;
            log.info("接口总耗时：" + lastTime);
            return new R().putData("no", no);
        }
        return R.empty();
    }

}
