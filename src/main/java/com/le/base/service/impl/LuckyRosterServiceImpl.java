package com.le.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyRoster;
import com.le.base.mapper.LuckyRosterMapper;
import com.le.base.service.ILuckyRosterService;
import com.le.core.rest.R;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public R findPage(Page<LuckyRoster> pagination, LuckyRoster search,Integer type,Long ruleId) {

        List<LuckyRoster> luckyRosterList = baseMapper.selectRoster(pagination, search,type,ruleId);
        pagination.setRecords(luckyRosterList);
        if (pagination == null) {
            return R.empty();
        }
        return R.success(pagination);
    }

    public R editData(LuckyRoster luckyRoster) {
        saveOrUpdate(luckyRoster);
        return R.success();
    }
}
