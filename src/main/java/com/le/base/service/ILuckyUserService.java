package com.le.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.le.base.entity.LuckyUser;
import com.le.core.rest.R;
import com.le.base.entity.dto.AisheliOrderDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cc
 * @since 2019-03-07
 */
public interface ILuckyUserService extends IService<LuckyUser> {

    String doDrawAndSaveUser(List<AisheliOrderDto> orders, Long ruleId);

    List<LuckyUser> getByRuleId(Long ruleId);
    /**
     * 后台分页
     *
     * @param pagination 分页参数
     * @param search     搜索条件
     * @return
     */
    R findPage(Page<LuckyUser> pagination, LuckyUser search,Long ruleId);

    /**
     * 添加或修改
     *
     * @param luckyUser 数据实体
     * @return
     */
    R editData(LuckyUser luckyUser);
}
