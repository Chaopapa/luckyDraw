package com.le.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyRule;
import com.le.base.mapper.LuckyRuleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.service.ILuckyRuleService;
import com.le.core.rest.R;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2019-03-07
 */
@Service
public class LuckyRuleServiceImpl extends ServiceImpl<LuckyRuleMapper, LuckyRule> implements ILuckyRuleService {

    @Override
    public R findPage(Page<LuckyRule> pagination, LuckyRule search) {
        QueryWrapper<LuckyRule> qw = new QueryWrapper<>();

        IPage<LuckyRule> page = baseMapper.selectPage(pagination, qw);

        if(page == null){
            return R.empty();
        }
        return R.success(page);
    }

    public R editData(LuckyRule luckyRule) {

        if(luckyRule.getLimitEndDate()!=null&&luckyRule.getLimitBeginDate()==null){
            return R.error("填入订单结束时间时，请先填写订单开始时间");
        }
        if(luckyRule.getEndDate()!=null&&luckyRule.getBeginDate()==null){
            return R.error("填入抽奖结束时间时，请先填写抽奖开始时间");
        }

        if(luckyRule.getLimitEndDate()!=null){

            if(luckyRule.getLimitEndDate().compareTo(luckyRule.getLimitBeginDate())<0){
               return R.error("订单的结束时间不能早于订单的开始时间");
            }
        }
        if(luckyRule.getEndDate()!=null){

            if(luckyRule.getEndDate().compareTo(luckyRule.getBeginDate())<0){
                return R.error("抽奖的结束时间不能早于抽奖的开始时间");

            }
        }
        if(luckyRule.getBeginDate()!=null&&luckyRule.getLimitBeginDate()!=null){
            if(luckyRule.getBeginDate().compareTo(luckyRule.getLimitBeginDate())<0){
                return R.error("抽奖的开始时间不能早于订单的开始时间");
            }
        }

        if(luckyRule.getLimitMinPrice()!=null&&luckyRule.getLimitMaxPrice()!=null){

            if(luckyRule.getLimitMaxPrice().compareTo(luckyRule.getLimitMinPrice())<0){
                return R.error("订单的最大金额不能小于最小金额");
            }
        }

        saveOrUpdate(luckyRule);
        return R.success();
    }
}
