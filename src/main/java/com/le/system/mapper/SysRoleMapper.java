package com.le.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.le.system.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @ClassName SysRoleMapper
 * @Author lz
 * @Description 角色Mapper
 * @Date 2018/10/9 11:35
 * @Version V1.0
 **/
public interface SysRoleMapper extends BaseMapper<SysRole> {

    Set<SysRole> findRolesByUserId(@Param("userId") Long userId);

    List<SysRole> findUserRole(@Param("userId") long userId);
}
