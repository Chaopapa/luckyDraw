package com.le.component.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.le.component.sms.entity.ComSmslog;
import com.le.component.sms.entity.enums.SmsType;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 短信记录 Mapper 接口
 * </p>
 *
 * @author 严秋旺
 * @since 2018-11-19
 */
public interface ComSmslogMapper extends BaseMapper<ComSmslog> {

    ComSmslog selectLastSmslog(@Param("mobile") String mobile, @Param("smsType") SmsType smsType);
}
