package com.le.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.component.geetest.GeetestConfig;
import com.le.component.geetest.GeetestLib;
import com.le.core.rest.R;
import com.le.system.entity.SysUser;
import com.le.system.entity.SysUserRole;
import com.le.system.mapper.SysUserMapper;
import com.le.system.service.ISysUserRoleService;
import com.le.system.service.ISysUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName SysUserServiceImpl
 * @Author lz
 * @Description 用户接口实现层
 * @Date 2018/10/9 11:33
 * @Version V1.0
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public R validate(HttpServletRequest request, String username, String password, boolean rememberMe) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());
        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        Integer gt_server_status_code = (Integer) request.getSession()
                .getAttribute(gtSdk.gtServerStatusSessionKey);
        String userid = (String) request.getSession().getAttribute("userid");
        int gtResult = 0;
        if (gt_server_status_code == 1) {
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, userid);
        } else {
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        if (gtResult == 1) {
            if (username != null && password != null) {
                SysUser user = findByUsername(username);
                if (user == null) {
                    return R.error("用户名或者密码不正确!");
                }
                if (!DigestUtil.md5Hex(password).equals(user.getPassword())) {
                    return R.error("用户名或者密码不正确!");
                }
                //使用权限工具进行用户登录，登录成功后跳到shiro配置的successUrl中，与下面的return没什么关系！
                UsernamePasswordToken token = new UsernamePasswordToken(username, DigestUtil.md5Hex(password), rememberMe);
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
            }
            return R.success();
        }
        return R.error("用户名或者密码不能为空!");
    }

    /**
     * @param username 账号
     * @return com.le.admin.entity.SysUser
     * @description 通过账号查找后台登良者
     * @author lz
     * @date 2018/10/11 10:39
     * @version V1.0.0
     */
    @Override
    public SysUser findByUsername(String username) {
        LambdaQueryWrapper<SysUser> lq = new QueryWrapper<SysUser>()
                .lambda().eq(SysUser::getUsername, username);
        SysUser user = this.getOne(lq);
        return user;
    }

    /**
     * @param roles 角色id user
     * @return com.le.base.util.R
     * @description 添加或修改用户
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R editData(SysUser user, List<Long> roles) {
        Long userId = user.getId();

        if (userId == null) {
            String password = user.getPassword();
            if (StrUtil.isBlank(password)) {
                return R.error("请输入密码");
            }
            user.setPassword(DigestUtil.md5Hex(password));
            user.setStatus(0);
            save(user);
            sysUserRoleService.replaceUserRole(user, roles);
        } else {
            updateById(user);
            sysUserRoleService.replaceUserRole(user, roles);
        }
        return R.success();
    }

    /**
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @description rbac 查找用户所有权限permission
     * @author lz
     * @date 2018/10/11 10:44
     * @version V1.0.0
     */
    @Override
    public List<String> findAuthorities(Long userId) {
        return baseMapper.findAuthorities(userId);
    }

    /**
     * @param username 用户账号
     * @return java.util.List<java.lang.String>
     * @description rbac 查找用户账号是否存在
     * @author lz
     * @date 2018/10/11 10:44
     * @version V1.0.0
     */
    @Override
    public boolean usernameExists(String username) {
        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Integer count = baseMapper.selectCount(qw);
        return count > 0 ? true : false;
    }

    /**
     * @param ids
     * @return com.le.base.util.R
     * @description 删除用户
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    public R del(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                delUserRole(id);
            }
            removeByIds(ids);
        }
        return R.success();
    }

    private void delUserRole(Long userId) {
        QueryWrapper<SysUserRole> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        sysUserRoleService.remove(qw);
    }

    @Override
    public R resetPassword(Long id) {
        SysUser user = baseMapper.selectById(id);
        if (user == null) {
            return R.error("重置失败");
        }
        String newPassword = DigestUtils.md5Hex("123456");
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return R.error();
    }

    /**
     * @param page   分页参数
     * @param search SysUser 对象
     * @return com.le.base.util.R
     * @description 后台用户分页
     * @author lz
     * @date 2018/10/11 10:12
     * @version V1.0.0
     */
    @Override
    public R findPage(Page<SysUser> page, SysUser search) {
        List<SysUser> list = baseMapper.findSysRole(page, search);
        page.setRecords(list);
        return R.success(page);
    }
}
