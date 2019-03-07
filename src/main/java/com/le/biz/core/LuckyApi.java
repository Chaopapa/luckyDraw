package com.le.biz.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.le.base.entity.LuckyRule;
import com.le.base.service.ILuckyRuleService;
import com.le.core.rest.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @GetMapping("getOne")
    public R getLuckyOne() {
        Date date = new Date();
        LuckyRule rule = luckyRuleService.getOne(new LambdaQueryWrapper<LuckyRule>().ge(LuckyRule::getBeginDate, date).ge(LuckyRule::getEndDate, date));
        if(null == rule){
            return R.error("请先设置活动规格");
        }
        Map<String,Object> paramMap = MapUtil.newHashMap();
        paramMap.put("beginDate", rule.getLimitBeginDate());
        paramMap.put("endDate", rule.getLimitEndDate());
        if(rule.getLimitMinPrice() != null && rule.getLimitMaxPrice() != null){
            paramMap.put("limitMinPrice",rule.getLimitMinPrice().toString());
            paramMap.put("limitMaxPrice",rule.getLimitMaxPrice().toString());
        }
        String orderStr = HttpUtil.post("http://192.168.100.13:8082/aisheli/oms/api/lucky/draw/getNo.jhtml", paramMap);
        if(StrUtil.isNotBlank(orderStr)){
            System.out.println(orderStr);
        }
        return R.success();
    }

}
