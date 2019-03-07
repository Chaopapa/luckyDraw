package com.le.core.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @ClassName ErrorPageConfig
 * @Author lz
 * @Description 404 500 403页面自定义
 * @Date 2018/10/15 15:18
 * @Version V1.0
 **/
@Component
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error401 = new ErrorPage(HttpStatus.FORBIDDEN, "/admin/login");
        //错误类型为404，找不到网页的，默认显示404.html网页
        ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
        //错误类型为500，表示服务器响应错误，默认显示500.html网页
        ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
        //错误类型为500，表示服务器响应错误，默认显示500.html网页
        ErrorPage error403 = new ErrorPage(HttpStatus.FORBIDDEN, "/error/403");

        registry.addErrorPages(error401, error404, error500, error403);
    }
}
