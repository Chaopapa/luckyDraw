package com.le.component.oss;

import cn.hutool.core.util.StrUtil;
import com.le.system.entity.SysOss;
import com.le.system.service.ISysOssService;
import com.le.core.rest.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName OssController
 * @Author lz
 * @Description oss上传
 * @Date 2018/10/17 8:59
 * @Version V1.0
 **/
@Controller
@RequestMapping("/admin/component/oss")
public class OSSController {

    @Autowired
    private ISysOssService sysOssService;

    /**
     * 上传文件
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public R upload(HttpServletRequest request){
        String urls = null;
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<String> files = new ArrayList<>();
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    String url = OSSFactory.build().upload(file);
                    //保存文件信息
                    SysOss oss = new SysOss();
                    oss.setUrl(url);
                    sysOssService.save(oss);
                    if (StrUtil.isNotBlank(url)) {
                        if (StrUtil.isBlank(urls)) {
                            urls = url;
                        } else {
                            urls = urls + "," + url;
                        }
                    } else {
                        return R.error("上传失败");
                    }
                }
            }
        }
        return R.success().putData("urls", urls);
    }
}
