package com.le.base.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyRoster;
import com.le.base.entity.LuckyRule;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.base.mapper.LuckyRosterMapper;
import com.le.base.service.ILuckyRosterService;
import com.le.base.service.ILuckyRuleService;
import com.le.core.rest.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public R findPage(Page<LuckyRoster> pagination, LuckyRoster search, Integer type, Long rule) {

        List<LuckyRoster> luckyRosterList = baseMapper.selectRoster(pagination, search, type, rule);
        pagination.setRecords(luckyRosterList);
        if (pagination == null) {
            return R.empty();
        }
        return R.success(pagination);
    }

    @Transactional
    public R editData(LuckyRoster luckyRoster) {
        if (luckyRoster.getSeq() == null) {
            luckyRoster.setSeq(999);
        }

        if (luckyRoster.getId() == null) {//新增时
            LuckyRule luckyRule = luckyRuleService.getById(luckyRoster.getRuleId());
            if (luckyRoster.getType().equals(RosterTypeEnum.Menu)) {//新增抽奖名单
                if (Boolean.FALSE.equals(luckyRule.getHaveMenu())) {
                    luckyRule.setHaveMenu(true);

                }

            }
            if (luckyRoster.getType().equals(RosterTypeEnum.Blacklist)) {//新增黑名单
                if (Boolean.FALSE.equals(luckyRule.getHaveBlacklist())) {
                   luckyRule.setHaveBlacklist(true);
                }
            }
            if (Boolean.TRUE.equals(luckyRule.getLimitNo())) {
               luckyRule.setLimitNo(false);
            }

            luckyRuleService.saveOrUpdate(luckyRule);

        }
        saveOrUpdate(luckyRoster);
        return R.success();
    }

    @Override
    public List<LuckyRoster> findByRuleId(Long ruleId, Boolean isHaveMenu, Boolean isHaveBlacklist) {
        if(Boolean.FALSE.equals(isHaveMenu) && Boolean.FALSE.equals(isHaveBlacklist)){
            return null;
        }
        LambdaQueryWrapper<LuckyRoster> lw = new QueryWrapper<LuckyRoster>().lambda();
        lw.eq(LuckyRoster::getRuleId, ruleId).orderByAsc(LuckyRoster::getSeq);
        if(!(Boolean.TRUE.equals(isHaveMenu) && Boolean.TRUE.equals(isHaveBlacklist))){
            if (Boolean.TRUE.equals(isHaveMenu)) {
                lw.eq(LuckyRoster::getType, RosterTypeEnum.Menu);
            } else if (Boolean.TRUE.equals(isHaveBlacklist)) {
                lw.eq(LuckyRoster::getType, RosterTypeEnum.Blacklist);
            }
        }
        return this.list(lw);
    }

    @Override
    @Transactional
    public void removeRosters(List<Long> ids, RosterTypeEnum type) {



        List<Long> ruleList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                LuckyRoster roster = this.getById(id);
                Long ruleId = roster.getRuleId();
                ruleList.add(ruleId);
            }
        }

        baseMapper.deleteBatchIds(ids);

        if (CollectionUtil.isNotEmpty(ruleList)) {
            Collection<LuckyRule> luckyRules = luckyRuleService.listByIds(ruleList);
            if (CollectionUtil.isNotEmpty(luckyRules)) {
                for (LuckyRule luckRule : luckyRules) {
                    QueryWrapper<LuckyRoster> qw = new QueryWrapper<>();
                    qw.eq("rule_id",luckRule.getId());
                    qw.eq("type",type);

                    LuckyRoster roster = getOne(qw);
                    if (roster == null) {
                        if (type.equals(RosterTypeEnum.Menu)) {
                            if (Boolean.TRUE.equals(luckRule.getHaveMenu())) {
                                luckRule.setHaveMenu(false);
                            }
                        }
                        if (type.equals(RosterTypeEnum.Blacklist)) {
                            if (Boolean.TRUE.equals(luckRule.getHaveBlacklist())) {
                                luckRule.setHaveBlacklist(false);
                            }
                        }
                        luckyRuleService.updateById(luckRule);
                    }
                }

            }

        }




    }
}
