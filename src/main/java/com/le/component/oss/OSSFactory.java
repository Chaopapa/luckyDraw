package com.le.component.oss;

import com.le.core.util.SpringContextUtil;
import com.le.system.service.ISysConfigService;

/**
 * @ClassName OSSFactory
 * @Author lz
 * @Description 文件上传Factory
 * @Date 2018/10/16 17:22
 * @Version V1.0
 **/
public final class OSSFactory {

    private static ISysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (ISysConfigService) SpringContextUtil.getBean("sysConfigService");
    }

    public static CloudStorageService build() {
        //获取云存储配置信息
        CloudStorageConfig config = sysConfigService.findConfig(CloudStorageConfig.class);

        if (config.getType() == CloudStorageConfigType.QINIU.getValue()) {
            return new QiniuCloudStorageService(config);
        } else if (config.getType() == CloudStorageConfigType.ALIYUN.getValue()) {
            return new AliyunCloudStorageService(config);
        }
        return null;
    }

}
