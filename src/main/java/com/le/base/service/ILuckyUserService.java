package com.le.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.le.base.entity.LuckyUser;
import com.le.base.entity.dto.AisheliOrderDto;

import java.util.List;

/**
 * @ClassName IUserService
 * @Author lz
 * @Description 用户信息接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ILuckyUserService extends IService<LuckyUser> {

    String doDrawAndSaveUser(List<AisheliOrderDto> orders, Long ruleId);

    List<LuckyUser> getByRuleId(Long ruleId);
}
