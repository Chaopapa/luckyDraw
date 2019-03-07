package com.le.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.le.core.rest.R;
import com.le.system.entity.SysRole;
import com.le.system.entity.SysRoleResource;
import com.le.system.entity.SysUserRole;
import com.le.system.mapper.SysRoleMapper;
import com.le.system.mapper.SysRoleResourceMapper;
import com.le.system.mapper.SysUserRoleMapper;
import com.le.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysRoleServiceImpl
 * @Author lz
 * @Description 角色接口实现层
 * @Date 2018/10/9 11:33
 * @Version V1.0
 **/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    /**
     * @param userId 用户id
     * @return com.le.admin.entity.SysRole
     * @description 通过userId 查找角色
     * @author lz
     * @date 2018/10/10 10:59
     * @version V1.0.0
     */
    @Override
    public Map<String, SysUserRole> findUserRoleMap(Long userId) {
        LambdaQueryWrapper<SysUserRole> lw = new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(lw);
        Map<String, SysUserRole> map = new HashMap<>();
        sysUserRoles.forEach(userRole -> map.put(userRole.getRoleId().toString(), userRole));
        return map;
    }

    /**
     * @param pagination, name 角色名
     * @return com.le.base.util.R
     * @description 后台角色分页
     * @author lz
     * @date 2018/10/11 10:12
     * @version V1.0.0
     */
    @Override
    public R manageData(Page<SysRole> pagination, String name) {
        QueryWrapper<SysRole> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            qw.like("nickname", name);
        }
        qw.orderByDesc("create_date");

        IPage<SysRole> page = this.page(pagination, qw);
        if (page == null) {
            return R.empty();
        }
        return R.success(page);
    }

    /**
     * @param role resourceIds
     * @return com.le.base.util.R
     * @description 添加或修改角色
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    public R editData(SysRole role, Long[] resourceIds) {
        Long id = role.getId();
        if (id != null) {
            deleteRoleResource(id);
            updateById(role);
        } else {
            save(role);
            id = role.getId();
        }
        if (ArrayUtil.isNotEmpty(resourceIds)) {
            for (Long permId : resourceIds) {
                SysRoleResource srr = new SysRoleResource();
                srr.setResourceId(permId);
                srr.setRoleId(id);
                sysRoleResourceMapper.insert(srr);
            }
        }
        return R.success();
    }

    @Override
    public R del(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                deleteRoleResource(id);
                delUserRole(id);
            }
            removeByIds(ids);
        }
        return R.success();
    }

    private void delUserRole(Long roleId) {
        QueryWrapper<SysUserRole> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId);
        sysUserRoleMapper.delete(qw);
    }

    private void deleteRoleResource(Long roleId) {
        QueryWrapper<SysRoleResource> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId);
        sysRoleResourceMapper.delete(qw);
    }

    @Override
    public List<SysRole> findUserRole(long userId) {
        return baseMapper.findUserRole(userId);
    }
}
