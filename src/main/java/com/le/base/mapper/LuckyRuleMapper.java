package com.le.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.le.base.entity.LuckyRule;
import org.apache.ibatis.annotations.Param;


public interface LuckyRuleMapper extends BaseMapper<LuckyRule> {

    LuckyRule searchByDate(@Param("nowDate")String nowDate);
}
