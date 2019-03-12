package com.le.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyRoster;
import com.le.base.entity.enums.RosterTypeEnum;
import com.le.core.rest.R;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈超
 * @since 2019-03-11
 */
public interface ILuckyRosterService extends IService<LuckyRoster> {

    /**
     * 后台分页
     *
     * @param pagination 分页参数
     * @param search     搜索条件
     * @return
     */
    R findPage(Page<LuckyRoster> pagination, LuckyRoster search,Integer type,Long rule);

    /**
     * 添加或修改
     *
     * @param luckyRoster 数据实体
     * @return
     */
    R editData(LuckyRoster luckyRoster);

    List<LuckyRoster> findByRuleId(Long ruleId, RosterTypeEnum type);
}
