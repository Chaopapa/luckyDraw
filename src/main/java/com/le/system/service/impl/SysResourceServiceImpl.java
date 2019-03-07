package com.le.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.le.core.base.Tree;
import com.le.core.rest.R;
import com.le.core.util.Constant;
import com.le.system.entity.SysResource;
import com.le.system.entity.SysRoleResource;
import com.le.system.entity.SysUser;
import com.le.system.entity.enums.ResourceType;
import com.le.system.mapper.SysResourceMapper;
import com.le.system.mapper.SysRoleResourceMapper;
import com.le.system.service.ISysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName SysResourceServiceImpl
 * @Author lz
 * @Description 资源接口实现层
 * @Date 2018/10/9 11:33
 * @Version V1.0
 **/
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    /**
     * @param
     * @return java.util.List<com.le.admin.entity.SysResource>
     * @description 查找带有子节点的菜单目录
     * @author lz
     * @date 2018/10/11 11:06
     * @version V1.0.0
     */
    @Override
    public List<SysResource> findChildrenList() {
        List<SysResource> list = this.list(null);
        List<SysResource> perentList = findParentList();
        List<SysResource> resourceList = getResourceList(list, perentList);
        return resourceList;
    }

    /**
     * @param list 所有节点 perentList 父节点
     * @return java.util.List<com.le.admin.entity.SysResource>
     * @description 递归 子节点到父节点资源上
     * @author lz
     * @date 2018/10/11 11:20
     * @version V1.0.0
     */
    private List<SysResource> getResourceList(List<SysResource> list, List<SysResource> perentList) {
        List<SysResource> resourceList = new ArrayList<>();
        perentList.stream().forEach(s -> {
            List<SysResource> childrenList = new ArrayList<>();
            list.stream().forEach(c -> {
                if (s.getId().equals(c.getParentId())) {
                    childrenList.add(c);
                    s.setChildren(childrenList);
                }
            });
            resourceList.add(s);
        });
        return resourceList;
    }

    /**
     * @param
     * @return java.util.List<com.le.admin.entity.SysResource>
     * @description 查找所有的父级资源
     * @author lz
     * @date 2018/10/11 11:12
     * @version V1.0.0
     */
    @Override
    public List<SysResource> findParentList() {
        QueryWrapper<SysResource> qw = new QueryWrapper<>();
        qw.eq("parent_id", "0").orderByAsc("seq").orderByDesc("create_date");
        List<SysResource> list = this.list(qw);
        return list;
    }

    /**
     * @return com.le.base.util.R
     * @description 后台资源数据
     * @author lz
     * @date 2018/10/11 10:12
     * @paramid 资源id
     * @version V1.0.0
     */
    @Override
    public R manageData(Long id) {
        SysResource resource = this.getById(id);
        return R.success().putData("resource", resource);
    }

    /**
     * @param sysResource
     * @return com.le.base.util.R
     * @description 添加或修改资源
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    public R editData(SysResource sysResource) {
        if (sysResource.getParentId() == null) {
            sysResource.setParentId(ROOT_ID);
        }

        if (ROOT_ID.equals(sysResource.getParentId())) {
            sysResource.setDeep(1);
        } else {
            SysResource parent = baseMapper.selectById(sysResource.getParentId());
            sysResource.setDeep(parent.getDeep() + 1);
        }

        saveOrUpdate(sysResource);
        return R.success();
    }

    /**
     * @param
     * @return Set<Tree>
     * @description 获取资源数据树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    public Set<Tree> tree() {
        QueryWrapper<SysResource> qw = new QueryWrapper<>();
        qw.orderByAsc("seq");
        List<SysResource> sysResources = this.list(qw);

        Set<Tree> trees = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(sysResources)) {
            sysResources.stream().forEach(s -> {
                Tree tree = new Tree();
                tree.setId(String.valueOf(s.getId()));
                tree.setpId(String.valueOf(s.getParentId()));
                tree.setName(s.getName());
                trees.add(tree);
            });
        }
        return trees;
    }

    /**
     * @param
     * @return java.lang.String
     * @description 获取资源数据树 json 字符串树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    public String jsonStrTree() {
        Set<Tree> trees = tree();
        String jsonStrTree = null;
        ObjectMapper mapper = new ObjectMapper();
        if (trees != null && trees.size() > 0) {
            try {
                jsonStrTree = mapper.writeValueAsString(trees);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return jsonStrTree;
    }

    /**
     * @param
     * @return com.le.base.util.R
     * @description 获取资源数据树 json 树
     * @author lz
     * @date 2018/10/11 10:14
     * @version V1.0.0
     */
    @Override
    public R jsonTree() throws JsonProcessingException {
        Set<Tree> trees = tree();
        if (trees != null && trees.size() > 0) {
            return R.success().putData("trees", trees);
        }
        return R.empty();
    }

    @Override
    public List<SysResource> queryByRoleId(Long roleId) {
        return sysResourceMapper.queryByRoleId(roleId);
    }

    @Override
    public String queryJsonTreeByRoleId(Long roleId) {
        List<SysResource> sysResources = queryByRoleId(roleId);
        ObjectMapper objectMapper = new ObjectMapper();
        String resources = null;
        if (CollectionUtil.isNotEmpty(sysResources)) {
            try {
                resources = objectMapper.writeValueAsString(sysResources);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return resources;
    }

    /**
     * @param permission
     * @return boolean
     * @description 校验资源是否存在
     * @author lz
     * @date 2018/10/11 17:11
     * @version V1.0.0
     */
    @Override
    public boolean permissionExists(String permission) {
        QueryWrapper<SysResource> qw = new QueryWrapper<>();
        qw.eq("permission", permission);
        Integer count = sysResourceMapper.selectCount(qw);
        if (count > 0) {
            return false;
        }
        return true;
    }

    /**
     * @param id 集合
     * @return boolean
     * @description 删除资源
     * @author lz
     * @date 2018/10/11 17:11
     * @version V1.0.0
     */
    @Override
    public R del(Long id) {
        if (id != null) {
            SysResource r = this.getById(id);
            if (r == null) {
                return R.error("菜单已被删除");
            }
            if (parentIsExists(id)) {
                return R.error("删除失败,请先删除子菜单");
            }
            removeById(id);
            delRoleResourceByResourceId(id);
        }
        return R.success();
    }

    private void delRoleResourceByResourceId(Long resourceId) {
        QueryWrapper<SysRoleResource> qw = new QueryWrapper<>();
        qw.eq("resource_id", resourceId);
        sysRoleResourceMapper.delete(qw);
    }

    private boolean parentIsExists(Long id) {
        QueryWrapper<SysResource> qw = new QueryWrapper<>();
        qw.eq("parent_id", id);
        int count = sysResourceMapper.selectCount(qw);
        return count > 0;
    }

    @Override
    public List<SysResource> findUserTree(SysUser user) {
        List<SysResource> list;
        List<SysResource> root = new ArrayList<>();
        Map<Long, SysResource> map = new HashMap<>();

        if (user == null || Constant.SUPER_ADMIN.equals(user.getId())) {
            QueryWrapper<SysResource> wrapper = new QueryWrapper<>();
            wrapper.eq("type", ResourceType.MENU.getValue());
            wrapper.orderByAsc("deep", "seq");
            list = baseMapper.selectList(wrapper);
        } else {
            list = baseMapper.findUserResourceList(user);
        }

        list.forEach(resource -> {
            SysResource parent = map.get(resource.getParentId());


            if (parent == null) {
                root.add(resource);
            } else {
                parent.addChild(resource);
            }

            map.put(resource.getId(), resource);
        });

        return root;
    }
}
