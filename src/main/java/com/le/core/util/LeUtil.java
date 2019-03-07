package com.le.core.util;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @ClassName LeUtil
 * @Author lz
 * @Description 工具类
 * @Date 2018/11/27 11:52
 * @Version V1.0
 **/
public class LeUtil {

    public static final byte[] key = "LONGELONGELONGEE".getBytes();

    public static String aesEncrypt(String content) {
        content = "test中文";
        // 构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        // 加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        // 返回加密值
        return encryptHex;
    }

    public static String aesDecrypt(String encryptHex) {
        // 构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex);
        // 返回解密值
        return decryptStr;
    }
}
