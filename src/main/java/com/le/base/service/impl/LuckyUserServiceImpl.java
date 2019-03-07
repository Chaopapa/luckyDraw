package com.le.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.base.entity.LuckyUser;
import com.le.base.mapper.LuckyUserMapper;
import com.le.base.service.ILuckyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LuckyUserServiceImpl extends ServiceImpl<LuckyUserMapper, LuckyUser> implements ILuckyUserService {

}
