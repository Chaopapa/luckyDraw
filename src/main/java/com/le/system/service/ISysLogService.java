package com.le.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.system.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.le.core.rest.R;

/**
 * @ClassName ISysLogService
 * @Author lz
 * @Description 系统日志接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ISysLogService extends IService<SysLog> {

    /**
     * @ClassName ISysLogService
     * @Author wxy
     * @Description //日志分页
     * @Date 2018/11/9/009 15:20
     * @Param [page]
     * @Version V1.0
     **/
    public R manageData(Page<SysLog> page);

}
