package com.le.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyUser;
import com.le.base.entity.dto.AisheliOrderDto;
import com.le.base.mapper.LuckyUserMapper;
import com.le.base.service.ILuckyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class LuckyUserServiceImpl extends ServiceImpl<LuckyUserMapper, LuckyUser> implements ILuckyUserService {

    @Override
    public String doDrawAndSaveUser(List<AisheliOrderDto> orders, Long ruleId) {
        Collections.shuffle(orders);
        Random random= new Random();
        int index = random.nextInt(orders.size());
        log.info("index:"+index);
        AisheliOrderDto dto = orders.get(index);
        LuckyUser luckyUser = new LuckyUser();
        luckyUser.setAddress(dto.getA())
                .setNo(dto.getO())
                .setName(dto.getN())
                .setPhone(dto.getP())
                .setRuleId(ruleId);
        save(luckyUser);
        log.info("lucky no :" + dto.getO());
        return dto.getO();
    }

    @Override
    public List<LuckyUser> getByRuleId(Long ruleId) {
        List<LuckyUser> list = this.list(new QueryWrapper<LuckyUser>().lambda().eq(LuckyUser::getRuleId, ruleId));
        return list;
    }

}
