package com.le.core.properties;

import com.le.core.util.Constant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName LongeProperties
 * @Author lz
 * @Description 项目配置
 * @Date 2018/10/18 13:47
 * @Version V1.0
 **/
@Component
@ConfigurationProperties(prefix = Constant.PREFIX)
public class LongeProperties {

    /**
     * 小程序appid
     */
    private String appid = "wx909e8b8a2eb232f9";

    /**
     * 小程序secret
     */
    private String secret = "2f18975d45855bdfa4fdada75ec58001";

    private String mchId = "1518713411";

    private String partnerKey = "longwanglongwanglongwanglongwang";

    //private String domain = "http://cxwoo.51vip.biz:37723/woju";
    private String domain = "https://woju.fjlonge.com/woju";

    private String notifyUrl = domain + "/wxPay/aappPay/payNotify";

    private String refundUrl = domain + "/wxPay/aappPay/refundNotify";

    private String certPath = "E:/cert/pub/apiclient_cert.p12";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public void setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }
}
