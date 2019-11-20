package com.kunbu.common.util.mail.raw;

import java.util.Map;

/**
 * 邮件统一配置信息
 *
 * @author: KunBu
 * @time: 2019/9/4 10:17
 * @description:
 */
public interface MailConfig {

    String getHost();

    String getUserName();

    String getPassword();

    Integer getPort();

    Map<String, String> getProperties();
}
