package com.le.core.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @ClassName Tree
 * @Author lz
 * @Description ztree 树节点
 * @Date 2018/10/10 11:06
 * @Version V1.0
 **/
public class Tree implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Tree> children;
    /**
     * 父节点
     */
    private String pId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
