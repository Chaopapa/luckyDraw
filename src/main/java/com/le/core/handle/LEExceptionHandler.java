package com.le.core.handle;

import com.le.core.exception.LEException;
import com.le.core.exception.TokenException;
import com.le.core.rest.R;
import com.le.core.rest.RCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Set;

/**
 * @ClassName LEExceptionHandler
 * @Author lz
 * @Description //异常处理器
 * @Date 2018/10/16 10:48
 * @Version V1.0
 **/
@Slf4j
@RestControllerAdvice
public class LEExceptionHandler {

    @ExceptionHandler({UnknownAccountException.class, IncorrectCredentialsException.class})
    public R handleShiroAccountException(Exception e) {
        log.error(e.getMessage(), e);
        return new R(RCode.accountOrPasswordError);
    }

    /**
     * 未登录
     *
     * @param response
     * @param e
     * @throws IOException
     */
    @ExceptionHandler({UnauthenticatedException.class})
    public void handleShiroAuthenException(HttpServletResponse response, UnauthenticatedException e) throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(401);
    }

    /**
     * 访问未授权的页面
     *
     * @param response
     * @param e
     * @throws IOException
     */
    @ExceptionHandler({UnauthorizedException.class})
    public void handleShiroAuthorException(HttpServletResponse response, UnauthorizedException e) throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(403);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(LEException.class)
    public R handleRRException(LEException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public R handleRRException(BindException e) {
        return R.error(e.getBindingResult());
    }

    /**
     * @param e 异常
     * @return R
     * @description 系统异常处理
     * @author lz
     * @date 2018/10/16 10:29
     * @version V1.0.0
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        //Hibernate Validator验证消息返回
        BindingResult result = null;
        StringBuilder errorMsg = new StringBuilder();
        if (e instanceof MethodArgumentNotValidException) {
            result = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            result = ((BindException) e).getBindingResult();
        } else if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            for (ConstraintViolation<?> violation : constraintViolations) {
                errorMsg.append(violation.getMessage()).append(",");
            }
            errorMsg.delete(errorMsg.length() - 1, errorMsg.length());
            return R.error(errorMsg.toString());
        }
        if (result != null) {
            for (ObjectError error : result.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage()).append(",");
            }
            errorMsg.delete(errorMsg.length() - 1, errorMsg.length());
            return R.error(errorMsg.toString());
        }
        return R.error(e.getMessage());
    }

    /**
     * 处理token自定义异常
     */
    @ExceptionHandler(TokenException.class)
    public R handleTokenException(TokenException e){
        if (e.getToken() == null) {
            return new R(RCode.tokenNone);
        } else if (e.getToken().isEmpty()) {
            return new R(RCode.tokenEmpty);
        } else {
            return new R(RCode.tokenExpired);
        }
    }

    /**
     * @param e 异常
     * @return R
     * @description 系统异常处理
     * @author lz
     * @date 2018/10/16 10:29
     * @version V1.0.0
     */
    @ExceptionHandler(RuntimeException.class)
    public R runtimeExceptionHandler(RuntimeException e) {
        log.error("未知异常", e);
        return R.error(e.getMessage());
    }

}
