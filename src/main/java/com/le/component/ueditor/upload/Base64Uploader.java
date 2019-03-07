package com.le.component.ueditor.upload;

import com.le.component.oss.OSSFactory;
import com.le.component.ueditor.PathFormat;
import com.le.component.ueditor.define.AppInfo;
import com.le.component.ueditor.define.BaseState;
import com.le.component.ueditor.define.FileType;
import com.le.component.ueditor.define.State;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class Base64Uploader {

    public static State save(String content, Map<String, Object> conf) {

        byte[] data = decode(content);

        long maxSize = ((Long) conf.get("maxSize")).longValue();
        if (data.length > maxSize) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");
        String filename = (String) conf.get("filename");
        String savePath = PathFormat.parse((String) conf.get("savePath"),
                filename);

        savePath = savePath + suffix;
        //String physicalPath = (String) conf.get("rootPath") + savePath;

        String url = OSSFactory.build().upload(data, filename);
        if (StringUtils.isNotBlank(url)) {
            State storageState = new BaseState();
            storageState.putInfo("url", url);
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
            return storageState;
        } else {
            return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
        }


    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}