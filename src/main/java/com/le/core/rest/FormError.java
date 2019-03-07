package com.le.core.rest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 严秋旺
 * @Date 2018-11-07 14:21
 * @Version V1.0
 **/
@Data
@Accessors(chain = true)
public class FormError {
    private String name;
    private String msg;
}
