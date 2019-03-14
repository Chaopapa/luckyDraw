package com.le.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyRule;
import com.le.base.mapper.LuckyRuleMapper;
import com.le.base.service.ILuckyRuleService;
import com.le.core.rest.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2019-03-07
 */
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class LuckyRuleServiceImpl extends ServiceImpl<LuckyRuleMapper, LuckyRule> implements ILuckyRuleService {

    @Override
    public R findPage(Page<LuckyRule> pagination, LuckyRule search) {
        QueryWrapper<LuckyRule> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(search.getName())) {
            qw.like("name", search.getName());
        }

        IPage<LuckyRule> page = baseMapper.selectPage(pagination, qw);

        if (page == null) {
            return R.empty();
        }
        return R.success(page);
    }

    @Transactional
    public R editData(LuckyRule luckyRule) {

        if(luckyRule.getId()==null){//规则新增时对抽奖名单和黑名单初始化
            luckyRule.setHaveMenu(false);
            luckyRule.setHaveBlacklist(false);
        }else{//修改时对订单号重复抽奖增加限制，限制为否
            LuckyRule haveMenuRule = getById(luckyRule.getId());
            if(Boolean.TRUE.equals(haveMenuRule.getHaveMenu())&&Boolean.TRUE.equals(luckyRule.getLimitNo())){
                return R.error("已有抽奖名单，不可设置订单号重复抽奖");
            }
        }

        if(luckyRule.getLimitMinPrice()==null){
            luckyRule.setLimitMinPrice(new  BigDecimal(0));
        }

        if (luckyRule.getLimitEndDate().compareTo(luckyRule.getLimitBeginDate()) < 0) {
            return R.error("订单的结束时间不能早于订单的开始时间");
        }
        if (luckyRule.getEndDate().compareTo(luckyRule.getBeginDate()) < 0) {
            return R.error("抽奖的结束时间不能早于抽奖的开始时间");
        }

        if (luckyRule.getBeginDate().compareTo(luckyRule.getLimitBeginDate()) < 0) {
            return R.error("抽奖的开始时间不能早于订单的开始时间");
        }
        if (luckyRule.getLimitMinPrice() != null && luckyRule.getLimitMaxPrice() != null) {

            if (luckyRule.getLimitMaxPrice().compareTo(luckyRule.getLimitMinPrice()) < 0) {
                return R.error("订单的最大金额不能小于最小金额");
            }
        }

        //如果有同时间段的抽奖活动进行拦截
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",luckyRule.getBeginDate());
        map.put("endTime",luckyRule.getEndDate());
        map.put("id",luckyRule.getId());

       LuckyRule rule =baseMapper.selectActivityByTime(map);
       if(rule!=null){
           return R.error("抽奖时间区间与活动"+"【"+rule.getName()+"】"+"时间冲突");

       }

        saveOrUpdate(luckyRule);


        if(luckyRule.getId()!=null){
           if(luckyRule.getLimitMinPrice()==null||luckyRule.getLimitMaxPrice()==null){
               baseMapper.setNullbyId(luckyRule,luckyRule.getId());
            }

        }
        return R.success();
    }

    @Override
    public void removeRules(List<Long> ids) {
         baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateMenuAndBlacklist(Map<String, Object> map) {
        baseMapper.updateMenuAndBlacklist(map);

    }

    @Override
    public LuckyRule getByTime() {
        Date date = new Date();
        String now = DateUtil.formatDateTime(date);
        LuckyRule luckyRule = searchByDate(now);
        return luckyRule;
    }

    private LuckyRule searchByDate(String nowDate) {
        return baseMapper.searchByDate(nowDate);
    }
}
