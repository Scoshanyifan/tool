package com.kunbu.common.util.tool.mail;

import com.kunbu.common.util.basic.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Date;

/**
 * 异常邮件发送工具
 *
 * @author: kunbu
 * @create: 2019-11-20 15:29
 **/
@Component
public class ExceptionMailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMailUtil.class);

    private static final MessageFormat EXCEPTION_MSG_FORMAT    = new MessageFormat("{0}\n{1}");

    @Autowired
    private MailSendUtil mailSendUtil;

    @Value("${exception.mail.to}")
    private String to;


    public void sendExceptionMail(Throwable error, String title, String content) {
        try {
            //subject
            String subject = title + DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_7);
            //tos
            String[] tos;
            if (to != null) {
                tos = to.split(",");
            } else {
                tos = new String[]{"1274462659@qq.com"};
            }
            //content
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            error.printStackTrace(new PrintStream(baos));
            Object[] arr = {content, baos.toString()};
            String exceptionMsg = EXCEPTION_MSG_FORMAT.format(arr);

            mailSendUtil.sendSimpleMail(tos, subject, exceptionMsg);
        } catch (Exception e) {
            LOGGER.error(">>> ExceptionMailUtil send mail error", e);
        }
    }

}
