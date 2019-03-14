package com.le.biz.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.le.base.entity.LuckyRoster;
import com.le.base.entity.LuckyRule;
import com.le.base.entity.LuckyUser;
import com.le.base.entity.dto.AisheliOrderDto;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.base.service.ILuckyRosterService;
import com.le.base.service.ILuckyRuleService;
import com.le.base.service.ILuckyUserService;
import com.le.core.properties.LongeProperties;
import com.le.core.rest.R;
import com.le.core.rest.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName LuckOrderApi
 * @Author lz
 * @Description //TODO
 * @Date 2019/3/6 16:49
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/biz/lucky")
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
        LuckyRule rule = luckyRuleService.getByTime();
        if (null == rule) {
            return R.error("请先设置活动规则");
        }
        // 活动规则id
        Long ruleId = rule.getId();
        List<String> nos = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        List<LuckyRoster> rosters = null;
        List<AisheliOrderDto> orders = new ArrayList<>();
        String no = null;

        // 获取限制单号和限制手机号
        if(!(Boolean.TRUE.equals(rule.getLimitNo()) && Boolean.TRUE.equals(rule.getLimitPhone()))){
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

        rosters = luckyRosterService.findByRuleId(ruleId, rule.getHaveMenu(), rule.getHaveBlacklist());

        if(CollectionUtil.isNotEmpty(rosters)){
            for (LuckyRoster roster : rosters) {
                if(RosterTypeEnum.Menu.equals(roster.getType())) {
                    if(!nos.contains(roster.getNo())){
                        AisheliOrderDto dto = new AisheliOrderDto();
                        dto.setA(roster.getAddress());
                        dto.setO(roster.getNo());
                        dto.setN(roster.getName());
                        dto.setP(roster.getPhone());
                        orders.add(dto);
                    }
                }else if(RosterTypeEnum.Blacklist.equals(roster.getType())){
                    phones.add(roster.getPhone());
                }
            }

            if(CollectionUtil.isNotEmpty(orders)){
                for (AisheliOrderDto dto : orders) {
                    if(nos.contains(dto.getO()) || phones.contains(dto.getP())){
                        continue;
                    }
                    luckyUserService.saveUser(dto, ruleId);
                    return new R().putData("no", dto.getO());
                }
            }
        }

        // 发送http请求去获取单号中奖
        Map<String, Object> paramMap = MapUtil.newHashMap();
        String beginTime = DateUtil.formatDateTime(rule.getLimitBeginDate());
        String endTime = DateUtil.formatDateTime(rule.getLimitEndDate());
        paramMap.put("beginDate", beginTime);
        paramMap.put("endDate", endTime);
        if (rule.getLimitMinPrice() != null && rule.getLimitMaxPrice() != null) {
            paramMap.put("limitMinPrice", rule.getLimitMinPrice().toString());
            paramMap.put("limitMaxPrice", rule.getLimitMaxPrice().toString());
        }

        if(CollectionUtil.isNotEmpty(nos)){
            paramMap.put("luckyNoArr", nos);
        }

        if(CollectionUtil.isNotEmpty(phones)){
            paramMap.put("luckyPhoneArr", phones);
        }

        String orderStr = HttpUtil.post(longeProperties.getOrderUrl(), paramMap);

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
            return new R().putData("no", no);
        }

        return R.empty();
    }

}
