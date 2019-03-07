package com.le.component.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.le.system.entity.config.SmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author 严秋旺
 * @Date 2018-11-16 17:01
 * @Version V1.0
 **/
@Slf4j
@Component
public class SmsUtil {
    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    static {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
    }

    /**
     * 发送验证码短信
     *
     * @param mobile    手机号
     * @param code      短信码
     * @param tplCode   短信模板
     * @param smsConfig
     * @return
     * @throws ClientException
     */
    public static SendSmsResponse sendSms(String mobile, String code, String tplCode, SmsConfig smsConfig) throws ClientException {

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfig.getAccessKeyId(), smsConfig.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsConfig.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(tplCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    /**
     * 通过模板发送阿里大于短信
     *
     * @param phone
     * @param tplCode
     * @param templateParam json格式参数
     * @return
     * @throws ClientException
     */
    public static void sendTplmsg(String phone, String tplCode, String templateParam, SmsConfig smsConfig) {
        Runnable run = () -> {
            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfig.getAccessKeyId(), smsConfig.getAccessKeySecret());
            try {
                DefaultProfile.addEndpoint("cn-hangzhou", product, domain);

                IAcsClient acsClient = new DefaultAcsClient(profile);
                //组装请求对象-具体描述见控制台-文档部分内容
                SendSmsRequest request = new SendSmsRequest();
                //必填:待发送手机号
                request.setPhoneNumbers(phone);
                //必填:短信签名-可在短信控制台中找到
                request.setSignName("集菜商城");
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode(tplCode);
                //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
                request.setTemplateParam(templateParam);

                //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
                //request.setSmsUpExtendCode("90997");
                //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
                request.setOutId("yourOutId");
                SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                if (!sendSmsResponse.getCode().equals("OK")) {
                    log.error("短信异常" + sendSmsResponse.getMessage() + sendSmsResponse.getCode());
                }
            } catch (ClientException e) {
                log.error("短信异常" + e.getMessage());
            }
        };

        new Thread(run).start();
    }

}
