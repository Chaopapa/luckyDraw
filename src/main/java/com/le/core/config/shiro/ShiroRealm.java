package com.le.core.config.shiro;

import com.le.core.util.Constant;
import com.le.system.entity.SysRole;
import com.le.system.entity.SysUser;
import com.le.system.service.ISysRoleService;
import com.le.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ShiroRealm
 * @Author lz
 * @Date 2018/10/9 21:58
 * @Version V1.0
 **/
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Lazy
    @Autowired
    private ISysUserService sysUserService;
    @Lazy
    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 授予角色和权限
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //授权
        log.debug("授予角色和权限");
        // 添加权限 和 角色信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        Long userId = user.getId();
        if (userId.equals(Constant.SUPER_ADMIN)) {
            // 超级管理员，添加所有角色、添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
        } else {
            // 普通用户，查询用户的角色，根据角色查询权限

            List<SysRole> roles = sysRoleService.findUserRole(userId);
            List<String> roleList = new ArrayList<>();

            for (SysRole role : roles) {
                roleList.add(role.getRole());
            }

            authorizationInfo.addRoles(roleList);

            List<String> authorities = sysUserService.findAuthorities(userId);
            if (authorities != null) {
                authorizationInfo.addStringPermissions(authorities);
            }
        }
        return authorizationInfo;
    }

    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //UsernamePasswordToken用于存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        log.info("用户登录认证：验证当前Subject时获取到token为：" + token);
        String username = token.getUsername();
        // 调用数据层
        SysUser user = sysUserService.findByUsername(username);

        log.debug("用户登录认证！用户信息user：" + user);
        if (user == null) {
            // 用户不存在
            throw new AccountException("帐号或密码不正确！");
        } else if (user.getStatus() == 1) {
            // 用户不存在
            throw new DisabledAccountException("此帐号已经设置为禁止登录！");
        } else {
            // 密码存在
            // 第一个参数 ，登陆后，需要在session保存数据
            // 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
            // 第三个参数 ，realm名字
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        }
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
