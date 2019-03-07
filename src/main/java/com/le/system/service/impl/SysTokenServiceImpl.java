package com.le.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.le.system.entity.SysToken;
import com.le.system.mapper.SysTokenMapper;
import com.le.core.redis.IRedisService;
import com.le.system.service.ISysTokenService;
import com.le.core.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName SysTokenServiceImpl
 * @Author lz
 * @Description 用户Token实现层
 * @Date 2018/10/9 11:33
 * @Version V1.0
 **/
@Service
public class SysTokenServiceImpl extends ServiceImpl<SysTokenMapper, SysToken> implements ISysTokenService {

    @Autowired
    private IRedisService redisService;

    /**
     * 24小时后过期
     */
    private final static long EXPIRE = 7 * 3600 * 24;

    @Override
    @Cacheable(cacheNames = "token", unless = "#result == null")
    public SysToken queryByToken(String token) {
        Object tokenObject = redisService.get(Constant.REDIS_TOKEN_KEY + token);
        if(tokenObject != null){
            String tokenStr = (String)tokenObject;
            return JSON.parseObject(tokenStr, SysToken.class);
        }
        LambdaQueryWrapper<SysToken> qw = new QueryWrapper<SysToken>().lambda().eq(SysToken::getToken, token);
        return this.getOne(qw);
    }
    @Override
    public void expireToken(Long userId) {
        SysToken tokenEntity = this.getOne(new QueryWrapper<SysToken>().lambda().eq(SysToken::getUserId, userId));
        if(tokenEntity != null){
            // 设置过期时间
            tokenEntity.setExpireTime(new Date());
            this.updateById(tokenEntity);
            redisService.remove(Constant.REDIS_TOKEN_KEY + tokenEntity.getToken());
        }
    }

    /**
     * @description 创建token
     * @author lz
     * @date 2018/10/18 11:40
     * @return java.lang.String
     * @version V1.0.0
     */
    private String generateToken(){
        return IdUtil.simpleUUID();
    }
}
