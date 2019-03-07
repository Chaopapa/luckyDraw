package com.le.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.le.core.base.Tree;
import com.le.core.rest.R;
import com.le.system.entity.SysResource;
import com.le.system.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * @ClassName ISysUserService
 * @Author lz
 * @Description 资源接口层
 * @Date 2018/10/9 11:32
 * @Version V1.0
 **/
public interface ISysResourceService extends IService<SysResource> {

    /**
     * 根节点id
     */
    Long ROOT_ID = 0L;

    /**
     * @param
     * @return java.util.List<com.le.admin.entity.SysResource>
     * @description 查找带有子节点的菜单目录
     * @author lz
     * @date 2018/10/11 11:06
     * @version V1.0.0
     */
    public List<SysResource> findChildrenList();

    /**
     * @param
     * @return java.util.List<com.le.admin.entity.SysResource>
     * @description 查找所有的父级资源
     * @author lz
     * @date 2018/10/11 11:12
     * @version V1.0.0
     */
    public List<SysResource> findParentList();

    /**
     * @return com.le.base.util.R
     * @description 后台资源数据
     * @author lz
     * @date 2018/10/11 10:12
     * @paramid 资源id
     * @version V1.0.0
     */
    public R manageData(Long id);

    /**
     * @param sysResource
     * @return com.le.base.util.R
     * @description 添加或修改资源
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    public R editData(SysResource sysResource);

    /**
     * @param
     * @return Set<Tree>
     * @description 获取资源数据树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    public Set<Tree> tree();

    /**
     * @param
     * @return java.lang.String
     * @description 获取资源数据树 json 字符串树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    public String jsonStrTree();

    /**
     * @param
     * @return com.le.base.util.R
     * @description 获取资源数据树 json 树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    public R jsonTree() throws JsonProcessingException;

    /**
     * @param
     * @return List<SysResource>
     * @description 角色id查询角色的所有权限
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    public List<SysResource> queryByRoleId(Long roleId);

    /**
     * @param
     * @return List<SysResource>
     * @description 角色id查询角色的所有权限 json字符串树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    public String queryJsonTreeByRoleId(Long roleId);

    /**
     * @param permission
     * @return boolean
     * @description 校验资源是否存在
     * @author lz
     * @date 2018/10/11 17:11
     * @version V1.0.0
     */
    public boolean permissionExists(String permission);

    /**
     * @param id
     * @return boolean
     * @description 删除资源
     * @author lz
     * @date 2018/10/11 17:11
     * @version V1.0.0
     */
    public R del(Long id);

    List<SysResource> findUserTree(SysUser user);
}
