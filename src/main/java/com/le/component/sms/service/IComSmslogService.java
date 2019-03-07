package com.le.component.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.le.component.sms.entity.ComSmslog;
import com.le.component.sms.entity.enums.SmsType;
import com.le.core.rest.R;

/**
 * <p>
 * 短信记录 服务类
 * </p>
 *
 * @author 严秋旺
 * @since 2018-11-19
 */
public interface IComSmslogService extends IService<ComSmslog> {

    /**
     * @param pagination 分页参数
     * @return com.le.base.util.R
     * @description 后台短信记录分页
     * @author 严秋旺
     * @date 2018-11-19
     * @version V1.0.0
     */
    R findPage(Page<ComSmslog> pagination, ComSmslog search);

    /**
     * 发送短信
     *
     * @param smsType 短信类型
     * @param mobile  手机号码
     */
    void sendSms(SmsType smsType, String mobile);

    /**
     * 查询
     *
     * @param mobile
     * @param smsType
     * @return
     */
    ComSmslog findLastSmslog(String mobile, SmsType smsType);

    /**
     * 更改短信标记
     *
     * @param smslog
     */
    void modifyFlag(ComSmslog smslog);
}
