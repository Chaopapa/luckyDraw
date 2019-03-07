package com.le.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyUser;
import com.le.base.mapper.LuckyUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.service.ILuckyUserService;
import com.le.core.rest.R;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2019-03-07
 */
@Service
public class LuckyUserServiceImpl extends ServiceImpl<LuckyUserMapper, LuckyUser> implements ILuckyUserService {

    @Override
    public R findPage(Page<LuckyUser> pagination, LuckyUser search) {
        QueryWrapper<LuckyUser> qw = new QueryWrapper<>();

        IPage<LuckyUser> page = baseMapper.selectPage(pagination, qw);

        if(page == null){
            return R.empty();
        }
        return R.success(page);
    }

    public R editData(LuckyUser bizLuckyUser) {
        saveOrUpdate(bizLuckyUser);
        return R.success();
    }
}
