package com.le.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.le.base.entity.LuckyRule;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


public interface LuckyRuleMapper extends BaseMapper<LuckyRule> {

    LuckyRule searchByDate(@Param("nowDate")String nowDate);

    LuckyRule selectActivityByTime(@Param("map") Map<String, Object> map);

    void setNullbyId(@Param("rule") LuckyRule luckyRule,@Param("id") Long id);
}
