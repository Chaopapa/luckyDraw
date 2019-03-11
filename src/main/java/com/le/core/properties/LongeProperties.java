package com.le.core.properties;

import com.le.core.util.Constant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName LongeProperties
 * @Author lz
 * @Description 项目配置
 * @Date 2018/10/18 13:47
 * @Version V1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = Constant.PREFIX)
public class LongeProperties {

    /**
     * 订单请求地址
     */
    private String orderUrl = "http://192.168.100.13:8082/aisheli/oms/api/lucky/draw/getNo.jhtml";

}
