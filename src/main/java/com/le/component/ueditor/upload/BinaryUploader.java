package com.le.component.ueditor.upload;

import com.le.component.oss.OSSFactory;
import com.le.component.ueditor.define.AppInfo;
import com.le.component.ueditor.define.BaseState;
import com.le.component.ueditor.define.FileType;
import com.le.component.ueditor.define.State;
import com.le.core.util.SpringContextUtil;
import com.le.system.entity.SysOss;
import com.le.system.service.ISysOssService;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    public static final State save(HttpServletRequest request,
                                   Map<String, Object> conf) {
        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;

        if (!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }

        ServletFileUpload upload = new ServletFileUpload(
                new DiskFileItemFactory());

        if (isAjaxUpload) {
            upload.setHeaderEncoding("UTF-8");
        }

        try {
            //SpringMVC的上传框架
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
            if (multipartFile == null) {
                return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
            }

            //获取配置文件的路径
            String savePath = (String) conf.get("savePath");
            //获取文件名
            String originFileName = multipartFile.getOriginalFilename();
            //获取文件的后缀

            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0,
                    originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            //判断上传文件的长度
            long maxSize = (int) conf.get("maxSize");
            byte[] bytes = multipartFile.getBytes();
            if (bytes.length > maxSize) {
                return new BaseState(false, AppInfo.MAX_SIZE);
            }

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            //解析路径
            String url = OSSFactory.build().upload(multipartFile);
            if (StringUtils.isNotBlank(url)) {
                //保存文件信息
                SysOss ossEntity = new SysOss();
                ossEntity.setUrl(url);
                ISysOssService sysOssService = SpringContextUtil.getBean(ISysOssService.class);
                sysOssService.save(ossEntity);
                State storageState = new BaseState();
                storageState.putInfo("url", url);
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
                return storageState;

            } else {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }
}
