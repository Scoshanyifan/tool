package com.kunbu.common.util.mail.test;

import com.google.common.base.Splitter;
import com.kunbu.common.util.ResultMap;
import com.kunbu.common.util.mail.CommonMail;
import com.kunbu.common.util.mail.MailSendUtil;
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
public class MailTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailTestController.class);

    @Autowired
    private MailSendUtil mailSendUtil;

    @PostMapping("/send")
    public ResultMap sendMail(@RequestParam String subject,
                              @RequestParam String text,
                              @RequestParam String tos,
                              @RequestParam(required = false) String ccs,
                              @RequestParam(required = false) String bccs,
                              @RequestParam(required = false) MultipartFile[] attachments) {

        List<String> toList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tos);
        CommonMail mail = new CommonMail((String[])toList.toArray(), subject, text);
        if (StringUtils.isNotBlank(ccs)) {
            List<String> ccList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(ccs);
            mail.setCcs((String[]) ccList.toArray());
        }
        if (StringUtils.isNotBlank(bccs)) {
            List<String> bccList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(bccs);
            mail.setBccs((String[]) bccList.toArray());
        }
        if (attachments != null && attachments.length > 0) {
            mail.setMultipartFiles(attachments);
        }
        try {
            mailSendUtil.sendMail(mail, true);
            return ResultMap.success();
        } catch (Exception e) {
            LOGGER.error(">>> sendMail error", e);
            return ResultMap.error("发送邮件失败，请重试");
        }
    }

}
