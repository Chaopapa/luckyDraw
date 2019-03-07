package com.le.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.le.system.entity.SysResource;
import com.le.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysResourceMapper
 * @Author lz
 * @Description 资源Mapper
 * @Date 2018/10/9 11:35
 * @Version V1.0
 **/
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> queryByRoleId(Long roleId);

    List<SysResource> findUserResourceList(@Param("user") SysUser user);
}
