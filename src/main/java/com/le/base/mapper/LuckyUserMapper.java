package com.le.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LuckyUserMapper extends BaseMapper<LuckyUser> {

    List<LuckyUser> selectLuckyDog(Page<LuckyUser> page, @Param("search") LuckyUser search);
}
