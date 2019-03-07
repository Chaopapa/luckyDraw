package com.le.base.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyRule;
import com.le.base.mapper.LuckyRuleMapper;
import com.le.base.service.ILuckyRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class LuckyRuleServiceImpl extends ServiceImpl<LuckyRuleMapper, LuckyRule> implements ILuckyRuleService {

    private static Map<String, LuckyRule> ruleMap = new HashMap<>();

    @Override
    public LuckyRule getByTime() {
        Date date = new Date();
        String now = DateUtil.formatDateTime(date);
        LuckyRule luckyRule = null;
        if (ruleMap == null || ruleMap.size() == 0) {
            luckyRule = searchByDate(now);
            if(luckyRule == null){
                return null;
            }
            String beginDate = DateUtil.formatDateTime(luckyRule.getBeginDate());
            String endDate = DateUtil.formatDateTime(luckyRule.getEndDate());
            ruleMap.put(beginDate + "@" + endDate, luckyRule);
            return luckyRule;
        } else {
            for (Iterator<Map.Entry<String, LuckyRule>> it = ruleMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, LuckyRule> item = it.next();
                String key = item.getKey();
                String[] time = key.split("@");
                Date beginDate = DateUtil.parseDateTime(time[0]);
                Date endDate = DateUtil.parseDateTime(time[1]);
                if (DateUtil.isIn(date, beginDate, endDate)) {
                    luckyRule = item.getValue();
                    break;
                } else {
                    luckyRule = searchByDate(now);
                    if(luckyRule == null){
                        return null;
                    }
                    it.remove();
                }
            }
        }
        return luckyRule;
    }

    private LuckyRule searchByDate(String nowDate) {
        return baseMapper.searchByDate(nowDate);
    }
}
