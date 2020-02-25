package com.kunbu.common.util.tool.mail.demo;

import com.google.common.base.Splitter;
import com.kunbu.common.util.ResultMap;
import com.kunbu.common.util.tool.mail.CommonMail;
import com.kunbu.common.util.tool.mail.ExceptionMailUtil;
import com.kunbu.common.util.tool.mail.MailSendUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-20 15:27
 **/
@RestController
@RequestMapping("/mail")
public class MailDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDemoController.class);

    @Autowired
    private MailSendUtil mailSendUtil;

    @Autowired
    private ExceptionMailUtil exceptionMailUtil;

    @PostMapping("/send")
    public ResultMap sendMail(@RequestParam String subject,
                              @RequestParam String text,
                              @RequestParam String tos,
                              @RequestParam(required = false) String ccs,
                              @RequestParam(required = false) String bccs,
                              @RequestParam(required = false) MultipartFile[] attachments,
                              @RequestParam(required = false) MultipartFile[] inlines) {

        List<String> toList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tos);
        CommonMail mail = new CommonMail(
                toList.toArray(new String[toList.size()]),
                subject,
                "<html><body><p>" + text + "</p><img src=\"cid:cat.png\" ></body></html>");
        mail.setHtml(true);

        if (StringUtils.isNotBlank(ccs)) {
            List<String> ccList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(ccs);
            mail.setCcs(ccList.toArray(new String[ccList.size()]));
        }
        if (StringUtils.isNotBlank(bccs)) {
            List<String> bccList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(bccs);
            mail.setBccs(bccList.toArray(new String[bccList.size()]));
        }
        if (attachments != null && attachments.length > 0) {
            mail.setAttachments1(attachments);
        }
        if (inlines != null && inlines.length > 0) {
            mail.setInlines1(inlines);
        }
        try {
            mailSendUtil.sendMail(mail, true);
        } catch (Exception e) {
            LOGGER.error(">>> sendMail error", e);
            return ResultMap.error("发送邮件失败，请重试");
        }

        try {
            Integer i = null;
            i.toString();
        } catch (Exception e) {
            exceptionMailUtil.sendExceptionMail(e, "测试报错", "异常内容：");
        }
        return ResultMap.success();
    }

}
