package com.le.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.le.base.entity.LuckyUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LuckyUserMapper extends BaseMapper<LuckyUser> {

    List<LuckyUser> selectLuckyDog(@Param("search") LuckyUser search);
}
