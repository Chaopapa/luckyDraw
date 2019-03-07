package com.le.system.mapper;

import com.le.system.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @ClassName SysConfigMapper
 * @Author lz
 * @Description 系统配置Mapper
 * @Date 2018/10/9 11:35
 * @Version V1.0
 **/
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * @description 根据关键字查询配置
     * @author lz
     * @date 2018/10/17 8:54
     * @param paramKey 关键字
     * @return com.le.admin.entity.SysConfig
     * @version V1.0.0
     */
    SysConfig findByKey(String paramKey);
}
