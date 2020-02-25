package com.kunbu.common.util.tool.mail;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.Arrays;

/**
 * 基于springboot的邮件服务
 *
 * https://blog.csdn.net/a286352250/article/details/53157963
 * https://www.jianshu.com/p/c79a9749a502
 *
 * @author: kunbu
 * @create: 2019-11-20 10:33
 **/
@Component
public class MailSendUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendUtil.class);

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void sendSimpleMail(String[] tos, String subject, String content) throws Exception {
        CommonMail mail = new CommonMail(tos, subject, content);
        sendMail(mail, false);
    }

    public void sendMail(CommonMail mail, boolean saveDB) throws Exception {
        LOGGER.info(">>> send mail:{}", mail);
        // check
        if (mail.getTos() == null) {
            return;
        }
        if (StringUtils.isAnyBlank(mail.getSubject(), mail.getText())) {
            return;
        }
        // send
        if (StringUtils.isBlank(mail.getEncoding())) {
            sendMimeMail(mail, CommonMail.ENCODING_UTF8);
        } else {
            sendMimeMail(mail, mail.getEncoding());
        }
        // TODO 保存数据库
        if (saveDB) {

        }
        LOGGER.info(">>> send mail finish");
    }

    /**
     * 发送复杂邮件
     *
     * @param mail
     * @param encoding
     **/
    private void sendMimeMail(CommonMail mail, String encoding) throws Exception {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, encoding);
        messageHelper.setFrom(getMailFrom());
        messageHelper.setTo(mail.getTos());
        messageHelper.setSubject(mail.getSubject());
        // true 表示启动HTML格式的邮件
        messageHelper.setText(mail.getText(), mail.isHtml());

        if (mail.getCcs() != null) {
            messageHelper.setCc(mail.getCcs());
        } else {
            // 把发送邮箱添加到抄送，避免网易邮箱识别为垃圾邮件（554 DT:SPM）
            messageHelper.setCc(getMailFrom());
        }
        if (mail.getBccs() != null) {
            messageHelper.setBcc(mail.getBccs());
        }
        // 附件
        if (mail.getAttachments1() != null) {
            for (MultipartFile file : mail.getAttachments1()) {
                messageHelper.addAttachment(file.getOriginalFilename(), file);
            }
        }
        if (mail.getAttachments2() != null) {
            for (CommonMail.MimeFile file : mail.getAttachments2()) {
                ByteArrayDataSource attachResource = new ByteArrayDataSource(file.getResource(), file.getType());
                messageHelper.addAttachment(file.getName(), attachResource);
            }
        }
        // 内嵌
        if (mail.getInlines1() != null) {
            for (MultipartFile file : mail.getInlines1()) {
                messageHelper.addInline(file.getOriginalFilename(), file, file.getContentType());
            }
        }
        if (mail.getInlines2() != null) {
            for (CommonMail.MimeFile file : mail.getInlines2()) {
                ByteArrayDataSource inlineResource = new ByteArrayDataSource(file.getResource(), file.getType());
                messageHelper.addInline(inlineResource.getName(), inlineResource);
            }
        }

        if (mail.getSendTime() != null) {
            messageHelper.setSentDate(mail.getSendTime());
        }

        javaMailSender.send(mailMessage);
        LOGGER.info(">>> sendMimeMail success, from:{}, to:{}", mailMessage.getFrom(), Arrays.toString(mail.getTos()));
    }

    private String getMailFrom() {

        return javaMailSender.getUsername();

    }
}
