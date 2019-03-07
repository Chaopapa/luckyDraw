package com.le.component.oss;

import com.le.core.validator.group.AliyunGroup;
import com.le.core.validator.group.QiniuGroup;
import com.le.system.entity.config.BaseConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName CloudStorageConfig
 * @Author lz
 * @Date 2018/10/16 17:28
 * @Version V1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CloudStorageConfig extends BaseConfig {

    /**
     * 类型 阿里云 七牛云
     */
    private Integer type;
    /**
     * 阿里云绑定的域名
     */
    @NotBlank(message = "阿里云绑定的域名不能为空", groups = AliyunGroup.class)
    private String aliyunDomain;
    /**
     * 阿里云路径前缀
     */
    private String aliyunPrefix;
    /**
     * 阿里云EndPoint
     */
    @NotBlank(message = "阿里云EndPoint不能为空", groups = AliyunGroup.class)
    private String aliyunEndPoint;
    /**
     * 阿里云AccessKeyId
     */
    @NotBlank(message = "阿里云AccessKeyId不能为空", groups = AliyunGroup.class)
    private String aliyunAccessKeyId;
    /**
     * 阿里云AccessKeySecret
     */
    @NotBlank(message = "阿里云AccessKeySecret不能为空", groups = AliyunGroup.class)
    private String aliyunAccessKeySecret;
    /**
     * 阿里云BucketName
     */
    @NotBlank(message = "阿里云BucketName不能为空", groups = AliyunGroup.class)
    private String aliyunBucketName;

    /**
     * 七牛云绑定的域名
     */
    @NotBlank(message = "七牛绑定的域名不能为空", groups = QiniuGroup.class)
    @URL(message = "七牛绑定的域名格式不正确", groups = QiniuGroup.class)
    private String qiniuDomain;
    /**
     * 七牛云路径前缀
     */
    private String qiniuPrefix;
    /**
     * 七牛云ACCESS_KEY
     */
    @NotBlank(message = "七牛AccessKey不能为空", groups = QiniuGroup.class)
    private String qiniuAccessKey;
    /**
     * 七牛云SECRET_KEY
     */
    @NotBlank(message = "七牛SecretKey不能为空", groups = QiniuGroup.class)
    private String qiniuSecretKey;
    /**
     * 七牛云存储空间名
     */
    @NotBlank(message = "七牛空间名不能为空", groups = QiniuGroup.class)
    private String qiniuBucketName;

}
