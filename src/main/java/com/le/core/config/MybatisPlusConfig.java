package com.le.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

/**
 * @ClassName MybatisPlusConfig
 * @Author lz
 * @Description Spring boot MybatisPlusConfig
 * @Date 2018/9/30 9:52
 * @Version V1.0
 **/
@Configuration
@MapperScan("com.le.**.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * sql 拦截器分析
     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor(){
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        performanceInterceptor.setMaxTime(30000);
//        performanceInterceptor.setFormat(true);
//        return performanceInterceptor;
//    }

    /**
     * 逻辑删 注入
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 乐观锁 配置
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
