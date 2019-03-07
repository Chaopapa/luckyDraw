package com.le.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.le.system.entity.SysToken;

/**
 * @ClassName ISysTokenService
 * @Author lz
 * @Description 用户Token接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ISysTokenService extends IService<SysToken> {

    /**
     * @description token查询用户Token
     * @author lz
     * @date 2018/10/18 11:25
     * @param token 值
     * @return com.le.core.entity.SysToken
     * @version V1.0.0
     */
    SysToken queryByToken(String token);

    /**
     * @description 用户id
     * @author lz
     * @date 2018/10/18 11:25
     * @param userId
     * @return com.le.core.entity.SysToken
     * @version V1.0.0
     */
    void expireToken(Long userId);
}
