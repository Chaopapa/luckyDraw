package com.le.base.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.le.base.entity.LuckyRule;
import com.le.core.rest.R;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cc
 * @since 2019-03-07
 */
public interface ILuckyRuleService extends IService<LuckyRule> {
    /**
     * 后台分页
     *
     * @param pagination 分页参数
     * @param search     搜索条件
     * @return
     */
    R findPage(Page<LuckyRule> pagination, LuckyRule search);

    LuckyRule getByTime();
    /**
     * 添加或修改
     *
     * @param luckyRule 数据实体
     * @return
     */
    R editData(LuckyRule luckyRule);


    /**
     * 添加或修改
     *
     * @param ids 规则id
     * @return
     */
    void removeRules(List<Long> ids);
}
