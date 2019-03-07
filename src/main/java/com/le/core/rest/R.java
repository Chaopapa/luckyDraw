package com.le.core.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>全局响应对象，有返回数据统一放在data属性上，使用方法 {@link R}.putData </p>
 *
 * @author 严秋旺
 * @since 2018/11/27 9:27
 **/
@Data
@Accessors(chain = true)
public class R implements Serializable {
    private static final long serialVersionUID = 1L;

    public R() {
        this(RCode.success);
    }

    public R(RCode code) {
        this(code.getValue(), code.getMsg());
    }

    public R(String code, String msg) {
        this.respCode = code;
        this.respMsg = msg;
    }

    /**
     * 响应数据
     */
    private Map<String, Object> data;

    /**
     * 响应码
     */
    private String respCode;

    /**
     * 响应信息
     */
    private String respMsg;
    private List<FormError> errors;

    public boolean isSuccess() {
        return RCode.success.getValue().equals(this.respCode);
    }

    public static R error() {
        return new R(RCode.fail);
    }

    public static R error(String msg) {
        return new R(RCode.fail).setRespMsg(msg);
    }

    public static R error(Errors errors) {
        List<FieldError> fieldErrors = errors.getFieldErrors();
        List<FormError> list = new ArrayList<>();

        fieldErrors.forEach(fieldError -> {
            FormError formError = new FormError();
            formError.setName(fieldError.getField()).setMsg(fieldError.getDefaultMessage());
            list.add(formError);
        });

        return error(list);
    }

    public static R error(List<FormError> errors) {
        if (errors.isEmpty()) {
            return new R(RCode.formError);
        }

        String msg = errors.get(0).getMsg();
        R r = new R(RCode.formError.getValue(), msg);
        r.setErrors(errors);
        return r;
    }

    public static R empty() {
        return new R(RCode.emptyData);
    }

    public static R success() {
        return new R(RCode.success);
    }

    public static R success(String msg) {
        return new R(RCode.success.getValue(), msg);
    }

    public static R success(IPage page) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        data.put("totalPages", page.getPages());
        data.put("pageSize", page.getSize());
        data.put("pageNumber", page.getCurrent());
        return new R(RCode.success).setData(data);
    }

    public R putData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put(key, value);
        return this;
    }
}
