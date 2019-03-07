package com.le.core.validator;

import com.le.core.exception.LEException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @ClassName ValidatorHandle
 * @Author lz
 * @Description hibernate-validator校验工具类
 * @Date 2018/10/16 11:20
 * @Version V1.0
 **/
public class ValidatorHandler {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws LEException  校验不通过，则报LEException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws LEException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new LEException(constraint.getMessage());
        }
    }
}
