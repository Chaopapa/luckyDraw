package com.le.core.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * @author lz
 * @since 2018/10/9 10:52
 **/
@Component
public class FreeMarkerConfig implements InitializingBean {

    @Autowired
    private Configuration configuration;
    @Autowired
    private ServletContext servletContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 加上这句后，可以在页面上使用shiro标签
        configuration.setSharedVariable("shiro", new ShiroTags());
        configuration.setSharedVariable("base", servletContext.getContextPath());
        configuration.setSharedVariable("baseRes", servletContext.getContextPath() + "/resources/webCom1.0");
    }
}
