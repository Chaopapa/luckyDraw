package com.le.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyRoster;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.base.mapper.LuckyRosterMapper;
import com.le.base.service.ILuckyRosterService;
import com.le.base.service.ILuckyRuleService;
import com.le.core.rest.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈超
 * @since 2019-03-11
 */
@Service
public class LuckyRosterServiceImpl extends ServiceImpl<LuckyRosterMapper, LuckyRoster> implements ILuckyRosterService {


    @Autowired
    private ILuckyRuleService luckyRuleService;

    @Override
    public R findPage(Page<LuckyRoster> pagination, LuckyRoster search,Integer type,Long rule) {

        List<LuckyRoster> luckyRosterList = baseMapper.selectRoster(pagination, search,type,rule);
        pagination.setRecords(luckyRosterList);
        if (pagination == null) {
            return R.empty();
        }
        return R.success(pagination);
    }
    @Transactional
    public R editData(LuckyRoster luckyRoster) {
        if(luckyRoster.getSeq()==null){
            luckyRoster.setSeq(999);
        }

        if(luckyRoster.getId()==null){//新增时
            Map<String,Object> map = new HashMap<>();
            map.put("ruleId",luckyRoster.getRuleId());
            if(luckyRoster.getType().equals(RosterTypeEnum.Menu)){
                map.put("haveMenu",true);
            }
            if(luckyRoster.getType().equals(RosterTypeEnum.Blacklist)){
                map.put("haveBlacklist",true);
            }

            luckyRuleService.updateMenuAndBlacklist(map);
        }
        saveOrUpdate(luckyRoster);
        return R.success();
    }

    @Override
    public List<LuckyRoster> findByRuleId(Long ruleId, RosterTypeEnum type) {
        LambdaQueryWrapper<LuckyRoster> lw = new QueryWrapper<LuckyRoster>()
                .lambda()
                .eq(LuckyRoster::getRuleId, ruleId)
                .orderByAsc(LuckyRoster::getSeq);
        if(type != null){
            lw.eq(LuckyRoster::getType, type);
        }
        return this.list(lw);
    }
}
