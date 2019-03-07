package com.le.component.email;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @ClassName MailService
 * @Author lz
 * @Description 邮箱发送
 * @Date 2018/12/12 9:35
 * @Version V1.0
 **/
@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String formMail;

    @Async
    public void sendSimpleMail(String toMail, String subject, String content, String attachmentFilename, String filePath) {
        try {
            MimeMessage msg = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
            helper.setFrom(formMail);
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(content);
            if(StrUtil.isNotBlank(filePath)){
                File file = new File(filePath);
                helper.addAttachment(attachmentFilename, file);
            }
            // 发送邮件
            sender.send(msg);
            log.info("发送给" + toMail + "简单邮件已经发送。 subject：" + subject);
        } catch (Exception e) {
            log.info("发送给" + toMail + "send mail error subject：" + subject);
            e.printStackTrace();
        }
    }

}
