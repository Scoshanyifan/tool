package com.kunbu.common.util.mail.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件工具类（静态版本，实际使用组件模式，避免每次生成邮件发送类）
 * 
 * https://blog.csdn.net/mingliangniwo/article/details/54972048
 * https://www.jianshu.com/p/5eb000544dd7 spring下
 *
 * ps: 如果是从云端获取的流，需要使用MailFileVO的话显示指定MIME TYPE
 * 
 */
public class JavaMailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailUtil.class);

    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String GBK_ENCODING = "GBK";

    /**
     * 发送文本邮件
     *
     * @param tos
     * @param subject
     * @param text
     * @param config
     * @author kunbu
     * @time 2019/9/4 10:31
     * @return
     **/
    public static void sendTextSimple(String[] tos, String subject, String text, MailConfig config) throws Exception {
        LOGGER.info(">>> start send email");
        JavaMailSenderImpl sender = initJavaMailSender(config);
        MimeMessage mailMessage = sender.createMimeMessage();
        initMimeMessageHelper(mailMessage, tos, sender.getUsername(), subject, text, false, false, DEFAULT_ENCODING);
        sender.send(mailMessage);
        LOGGER.info(">>> email send success");
    }

    public static void sendTextWithHtml(
            MailConfig config, String[] toMails, String subject, String text) throws Exception {

        sendAllWithMailFile(config, toMails, subject, text, null, null, DEFAULT_ENCODING);
    }

    public static void sendTextWithImg(
            MailConfig config, String[] toMails, String subject, String text, MailFileVO img) throws Exception {

        sendAllWithMailFile(config, toMails, subject, text, img, null, DEFAULT_ENCODING);
    }

    public static void sendTextWithImgCN(
            MailConfig config, String[] toMails, String subject, String text, MailFileVO img) throws Exception {

        sendAllWithMailFile(config, toMails, subject, text, img, null, GBK_ENCODING);
    }

    public static void sendAllWithFile(
            MailConfig config,
            String[] toMails,
            String subject,
            String text,
            File img,
            File[] attachs,
            String encoding) throws Exception {

        LOGGER.info(">>> start send email");
        JavaMailSenderImpl sender = initJavaMailSender(config);
        MimeMessage mailMessage = sender.createMimeMessage();
        MimeMessageHelper messageHelper = initMimeMessageHelper(
                mailMessage, toMails, sender.getUsername(), subject, text, true, true, encoding);

        // 插入图片（getName必须是包含后缀，不能是中文）
        if (img != null) {
            messageHelper.addInline(img.getName(), img);
        }
        // 插入附件
        if (attachs != null) {
            for (File attach : attachs) {
                messageHelper.addAttachment(attach.getName(), attach);
            }
        }

        // 发送邮件
        sender.send(mailMessage);
        LOGGER.info(">>> send email success!!!");
    }

    /**
     * 通过自定义文件类MileFile进行发送，其中资源是流的形式，但是这样jar包中就需要找到MIME类型，
     * 所以MileFile中需要指定，不然发送会报错，找不到contentType
     * @param config
     * @param toMails
     * @param subject
     * @param text
     * @param img
     * @param attachs
     * @param encoding
     * @throws Exception
     */
    public static void sendAllWithMailFile(
            MailConfig config,
            String[] toMails,
            String subject,
            String text,
            MailFileVO img,
            MailFileVO[] attachs,
            String encoding) throws Exception {

        LOGGER.info(">>> start send email");
        JavaMailSenderImpl sender = initJavaMailSender(config);
        MimeMessage mailMessage = sender.createMimeMessage();
        MimeMessageHelper messageHelper = initMimeMessageHelper(
                mailMessage, toMails, sender.getUsername(), subject, text, true, true, encoding);

        // 插入图片
        if (img != null) {
            ByteArrayDataSource imgResource = new ByteArrayDataSource(img.getResource(), img.getType());
            messageHelper.addInline(img.getName(), imgResource);
        }
        // 插入附件
        if (attachs != null) {
            for (MailFileVO attach : attachs) {
                ByteArrayDataSource attachResource = new ByteArrayDataSource(attach.getResource(), attach.getType());
                messageHelper.addAttachment(attach.getName(), attachResource);
            }
        }

        // 发送邮件
        sender.send(mailMessage);
        LOGGER.info(">>> send email success!!!");
    }

    /**
     * 使用FileSystemResource进行文件加载处理，jar包会对文件进行解析，特别是后缀，通过后缀找到MIME对应的类型
     * 使用MultipartFile，spring会进行文件加载
     * @param config
     * @param toMails
     * @param subject
     * @param text
     * @param img
     * @param img
     * @param attachs
     * @param encoding
     * @throws Exception
     */
    public static void sendAllWithMultipartFile(
            MailConfig config,
            String[] toMails,
            String subject,
            String text,
            MultipartFile img,
            MultipartFile[] attachs,
            String encoding) throws Exception {

        LOGGER.info(">>> start send email");
        JavaMailSenderImpl sender = initJavaMailSender(config);
        MimeMessage mailMessage = sender.createMimeMessage();
        MimeMessageHelper messageHelper = initMimeMessageHelper(
                mailMessage, toMails, sender.getUsername(), subject, text, true, true, encoding);

        // 插入图片
        if (img != null) {
            //图片插入正文中，使用addInLine
            messageHelper.addInline(img.getOriginalFilename(), img, img.getContentType());
        }
        // 插入附件
        if (attachs != null) {
            //附件使用addAttachment
            for (MultipartFile attach : attachs) {
                messageHelper.addAttachment(attach.getOriginalFilename(), attach);
            }
        }

        // 发送邮件
        sender.send(mailMessage);
        LOGGER.info(">>> send email success!!!");
    }

    /**
     * 初始化邮件特性
     * @param mailMessage
     * @param toMails
     * @param from
     * @param subject
     * @param text
     * @param isHTML
     * @param multipart
     * @param encoding
     * @return
     * @throws MessagingException
     */
    private static MimeMessageHelper initMimeMessageHelper(
            MimeMessage mailMessage,
            String[] toMails,
            String from,
            String subject,
            String text,
            boolean isHTML,
            boolean multipart,
            String encoding) throws MessagingException {

        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, multipart, encoding);
        messageHelper.setFrom(from);
        // 把发送邮箱添加到抄送，避免网易邮箱识别为垃圾邮件（554 DT:SPM）
        messageHelper.setCc(from);
        messageHelper.setTo(toMails);
        messageHelper.setSubject(subject);
        // true 表示启动HTML格式的邮件
        messageHelper.setText(text, isHTML);

        return messageHelper;
    }

    /**
     * 初始化邮件发送类
     * @param config
     * @return
     */
    private static JavaMailSenderImpl initJavaMailSender(MailConfig config) {
        if (config == null) {
            throw new RuntimeException("mail config null");
        }

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties properties = new Properties();
        if (config.getProperties() == null) {
            properties.setProperty("mail.smtp.starttls.enable", "false");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.timeout", "25000");
        } else {
            for (Map.Entry<String, String> entry : config.getProperties().entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
        }
        javaMailSender.setJavaMailProperties(properties);

        javaMailSender.setHost(config.getHost());
        javaMailSender.setUsername(config.getUserName());
        javaMailSender.setPassword(config.getPassword());
        javaMailSender.setPort(config.getPort());
        javaMailSender.setDefaultEncoding(DEFAULT_ENCODING);
        return javaMailSender;
    }


}
