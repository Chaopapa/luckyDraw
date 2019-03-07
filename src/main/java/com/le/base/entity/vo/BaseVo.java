package com.le.base.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BaseVo
 * @Author lz
 * @Description //保存相似列表值
 * @Date 2019/1/9 14:34
 * @Version V1.0
 **/
@Data
public class BaseVo implements Serializable {

    private static final long serialVersionUID = 7893126029046671238L;

    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 品类名称
     */
    private String kindName;

    /**
     * 标签
     */
    private String label;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 图片 首图
     */
    private String images;
}
