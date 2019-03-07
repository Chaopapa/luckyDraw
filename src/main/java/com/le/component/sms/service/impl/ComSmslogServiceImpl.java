package com.le.component.sms.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.component.sms.entity.ComSmslog;
import com.le.component.sms.entity.enums.SmsType;
import com.le.component.sms.mapper.ComSmslogMapper;
import com.le.component.sms.service.IComSmslogService;
import com.le.component.sms.util.SmsUtil;
import com.le.core.rest.R;
import com.le.core.util.JsonUtils;
import com.le.system.entity.config.SmsConfig;
import com.le.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 短信记录 服务实现类
 * </p>
 *
 * @author 严秋旺
 * @since 2018-11-19
 */
@Slf4j
@Service
public class ComSmslogServiceImpl extends ServiceImpl<ComSmslogMapper, ComSmslog> implements IComSmslogService {

    @Autowired
    private ISysConfigService configService;

    @Override
    public R findPage(Page<ComSmslog> pagination, ComSmslog search) {
        QueryWrapper<ComSmslog> qw = new QueryWrapper<>();
        qw.orderByDesc("create_date");

        if (StringUtils.isNotEmpty(search.getMobile())) {
            qw.like("mobile", "%" + search.getMobile() + "%");
        }

        if (search.getType() != null) {
            qw.eq("type", search.getType());
        }

        IPage<ComSmslog> page = baseMapper.selectPage(pagination, qw);

        if (page == null) {
            return R.empty();
        }
        return R.success(page);
    }


    @Override
    @Async
    public void sendSms(SmsType smsType, String mobile) {
        SmsConfig smsConfig = configService.findConfig(SmsConfig.class);

        String captcha = RandomStringUtils.randomNumeric(6);
        ComSmslog smslog = new ComSmslog();
        smslog.setCaptcha(captcha);
        Date now = new Date();
        smslog.setCreateDate(now);
        if (smsConfig.getCaptchaExpires() > 0) {
            Date endTime = DateUtils.addSeconds(now, smsConfig.getCaptchaExpires());
            smslog.setEndTime(endTime);
        }
        smslog.setFlag(0);
        smslog.setSignName(smsConfig.getSignName());
        smslog.setType(smsType);
        smslog.setTplCode(smsConfig.getCodeTplCode());
        smslog.setMobile(mobile);

        try {
            send(smslog, smsConfig);
        } finally {
            baseMapper.insert(smslog);
        }
    }

    private boolean send(ComSmslog smslog, SmsConfig smsConfig) {
        try {
            SendSmsResponse sendSmsResponse = SmsUtil.sendSms(smslog.getMobile(), smslog.getCaptcha(), smslog.getTplCode(), smsConfig);
            String json = JsonUtils.toJson(sendSmsResponse);
            log.debug("短信发送结果：" + json);
            smslog.setResult(json);
            String code = sendSmsResponse.getCode();

            if ("OK".equalsIgnoreCase(code)) {
                smslog.setSendTime(new Date());
            } else {
                log.warn("短信发送失败，" + sendSmsResponse.getMessage());
            }
        } catch (ClientException e) {
            log.error("短信发送失败，" + e.getMessage());

            if (log.isDebugEnabled()) {
                log.debug("短信发送失败", e);
            }
        }
        return false;
    }

    @Override
    public ComSmslog findLastSmslog(String mobile, SmsType smsType) {
        return baseMapper.selectLastSmslog(mobile, smsType);
    }

    @Override
    public void modifyFlag(ComSmslog smslog) {
        ComSmslog sms = new ComSmslog();
        sms.setId(smslog.getId());
        sms.setFlag(smslog.getFlag());
        baseMapper.updateById(sms);
    }
}
