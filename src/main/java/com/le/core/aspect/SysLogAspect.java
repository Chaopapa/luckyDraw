package com.le.core.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.le.core.annotation.SysLog;
import com.le.system.service.ISysLogService;
import com.le.core.config.shiro.ShiroUtil;
import com.le.core.util.HttpContextUtils;
import com.le.core.util.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author lz
 * @ClassName SysLogAspect
 * @description 系统日志，切面处理类
 * @Version V1.0
 * @since 2018/10/9 15:30
 **/
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private ISysLogService logService;

    @Pointcut("@annotation(com.le.core.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        com.le.system.entity.SysLog sysLog = new com.le.system.entity.SysLog();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new ObjectMapper().writeValueAsString(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        //用户名
        String username = ShiroUtil.getUsername();
        sysLog.setUsername(username);

        sysLog.setTime(time);
        //保存系统日志
        logService.save(sysLog);
    }
}
