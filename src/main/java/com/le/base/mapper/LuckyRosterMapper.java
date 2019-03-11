package com.le.base.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.base.entity.LuckyRoster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 陈超
 * @since 2019-03-11
 */
public interface LuckyRosterMapper extends BaseMapper<LuckyRoster> {

    List<LuckyRoster> selectRoster(Page<LuckyRoster> pagination, @Param("search") LuckyRoster search,@Param("type") Integer type,@Param("ruleId") Long ruleId);
}
