package com.le.core.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtils {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取表单分页对象
     *
     * @param <T>
     * @return
     */
    public static <T> Page<T> getPage() {
        HttpServletRequest request = getHttpServletRequest();
        long current = 1, size = 10;
        String paramCurrent = request.getParameter("pageNumber");
        String paramSize = request.getParameter("pageSize");

        if (StringUtils.isNotEmpty(paramCurrent)) {
            try {
                current = Long.parseLong(paramCurrent);
            } catch (Exception e) {
                //ignore
            }
        }

        if (StringUtils.isNotEmpty(paramSize)) {
            try {
                size = Long.parseLong(paramSize);
            } catch (Exception e) {
                //ignore
            }
        }

        return new Page<>(current, size);
    }
}
