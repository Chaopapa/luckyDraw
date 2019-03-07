package com.le.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.system.entity.SysLog;
import com.le.system.mapper.SysLogMapper;
import com.le.system.service.ISysLogService;
import com.le.core.rest.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SysLogServiceImpl
 * @Author lz
 * @Description 日志接口实现层
 * @Date 2018/10/9 11:33
 * @Version V1.0
 **/
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    /**
     * @ClassName ISysLogService
     * @Author wxy
     * @Description //日志分页
     * @Date 2018/11/9/009 15:20
     * @Param [page]
     * @Version V1.0
     **/
    @Override
    public R manageData(Page<SysLog> page) {
        List<SysLog> list = this.list(null);
        if (page!=null && CollectionUtil.isNotEmpty(list)){
            return R.success(page);
        }
        return R.empty();
    }
}
