package com.le.biz.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.le.base.entity.LuckyRule;
import com.le.base.entity.LuckyUser;
import com.le.base.entity.dto.AisheliOrderDto;
import com.le.base.service.ILuckyRuleService;
import com.le.base.service.ILuckyUserService;
import com.le.biz.TokenValidatorHandler;
import com.le.core.rest.R;
import com.le.core.rest.RCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LuckOrderApi
 * @Author lz
 * @Description //TODO
 * @Date 2019/3/6 16:49
 * @Version V1.0
 **/
@RestController
@RequestMapping("/templates/admin/biz/lucky")
public class LuckyApi {

    @Autowired
    private ILuckyRuleService luckyRuleService;

    @Autowired
    private ILuckyUserService luckyUserService;

    @GetMapping("get")
    public R getLuckyOne() {
        Date date = new Date();
        long interfaceTime = date.getTime();
        LuckyRule rule = luckyRuleService.getByTime();
        if (null == rule) {
            return R.error("请先设置活动规格");
        }
        Map<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put("beginDate", rule.getLimitBeginDate());
        paramMap.put("endDate", rule.getLimitEndDate());
        if (rule.getLimitMinPrice() != null && rule.getLimitMaxPrice() != null) {
            paramMap.put("limitMinPrice", rule.getLimitMinPrice().toString());
            paramMap.put("limitMaxPrice", rule.getLimitMaxPrice().toString());
        }
        Long beginTime = new Date().getTime();
        String orderStr = HttpUtil.post("http://192.168.100.13:8082/aisheli/oms/api/lucky/draw/getNo.jhtml", paramMap);
        Long totalConsumedTime = new Date().getTime() - beginTime;
        System.out.println("订单接口耗时：" + totalConsumedTime);
        if (StrUtil.isNotBlank(orderStr)) {
            R r = JSON.parseObject(orderStr, R.class);
            if (RCode.success.getValue().equals(r.getRespCode())) {
                Map<String, Object> data = r.getData();
                String order = MapUtil.getStr(data, "orders");
                List<AisheliOrderDto> orders = JSON.parseArray(order, AisheliOrderDto.class);
                String no = luckyUserService.doDrawAndSaveUser(orders);
                Long lastTime = new Date().getTime() - interfaceTime;
                System.out.println("接口总耗时：" + lastTime);
                return new R().putData("no", no);
            }
        }
        return R.error();
    }

}
