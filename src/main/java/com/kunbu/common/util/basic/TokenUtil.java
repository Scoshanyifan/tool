package com.kunbu.common.util.basic;


/**
 * token工具类
 *
 * @author kunbu
 * @time 2019/11/20 17:15
 * @return
 **/
public class TokenUtil {

    /** token类型-管理 */
    private static final String TOKEN_SERVICE_TYPE_MANAGE           = "MANAGE";
    /** 用户类型-管理员 */
    private static final String TOKEN_PERSON_TYPE_ADMIN             = "ADMIN";
    /** token格式：MAN_ADMIN_20190809_K07JHU1Y32G84 */
    public static final String FORMAT_PATTERN_ADMIN                 = "%s_%s_%s_%s";

    /** token类型-业务 */
    private static final String TOKEN_SERVICE_TYPE_BUSINESS         = "BUSINESS";
    /** 业务类型-app */
    private static final String TOKEN_PLATFORM_TYPE_APP             = "APP";
    /** 业务类型-web */
    private static final String TOKEN_PLATFORM_TYPE_WEB             = "WEB";
    /** 用户类型-普通用户 */
    private static final String TOKEN_PERSON_TYPE_USER              = "USER";
    /** token格式：BIZ_WEB_USER_2019090501_LN30DI7H8ESF */
    public static final String FORMAT_PATTERN_USER                  = "%s_%s_%s_%s_%s";

    private static final String SPLITTER                            = "_";
    private static final String WILDCARD                            = "*";

    /**
     * 业务系统中的web端用户token
     * BIZ_WEB_USER_20190905001_J0CXB87H97GH13
     * <p>
     * TODO 之后如果需要可在原有基础上追加
     *
     * @param userId
     * @param sessionId
     * @return
     * @author kunbu
     * @time 2019/9/5 15:51
     **/
    public static String generateUserWebToken(String userId, String sessionId) {
        String BIZ_WEB_USER_TOKEN = String.format(
                FORMAT_PATTERN_USER,
                TOKEN_SERVICE_TYPE_BUSINESS,
                TOKEN_PLATFORM_TYPE_WEB,
                TOKEN_PERSON_TYPE_USER,
                userId,
                sessionId);
        return BIZ_WEB_USER_TOKEN;
    }

    /**
     * 业务系统中的app端用户token
     * BIZ_APP_USER_20190905001_J0CXB87H97GH13
     *
     * @param userId
     * @param sessionId
     * @return
     * @author kunbu
     * @time 2019/9/5 15:51
     **/
    public static String generateUserAppToken(String userId, String sessionId) {
        String BIZ_APP_USER_TOKEN = String.format(
                FORMAT_PATTERN_USER,
                TOKEN_SERVICE_TYPE_BUSINESS,
                TOKEN_PLATFORM_TYPE_APP,
                TOKEN_PERSON_TYPE_USER,
                userId,
                sessionId);
        return BIZ_APP_USER_TOKEN;
    }

    /**
     * 后台系统管理员token
     *
     * @param userId
     * @param sessionId
     * @return
     * @author kunbu
     * @time 2019/9/5 18:10
     **/
    public static String generateAdminManToken(String userId, String sessionId) {
        String MAN_ADMIN_TOKEN = String.format(
                FORMAT_PATTERN_ADMIN,
                TOKEN_SERVICE_TYPE_MANAGE,
                TOKEN_PERSON_TYPE_ADMIN,
                userId,
                sessionId);
        return MAN_ADMIN_TOKEN;
    }

    /**
     * 匹配业务系统中web端所有用户
     *
     * @return
     * @author kunbu
     * @time 2019/9/5 16:08
     **/
    public static String getWildCardBizWeb() {
        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN_SERVICE_TYPE_BUSINESS)
                .append(SPLITTER)
                .append(TOKEN_PLATFORM_TYPE_WEB)
                .append(SPLITTER)
                .append(TOKEN_PERSON_TYPE_USER)
                .append(SPLITTER)
                .append(WILDCARD);
        return builder.toString();
    }

    /**
     * 匹配业务系统中app端所有用户
     *
     * @return
     * @author kunbu
     * @time 2019/9/5 16:08
     **/
    public static String getWildCardBizApp() {
        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN_SERVICE_TYPE_BUSINESS)
                .append(SPLITTER)
                .append(TOKEN_PLATFORM_TYPE_APP)
                .append(SPLITTER)
                .append(TOKEN_PERSON_TYPE_USER)
                .append(SPLITTER)
                .append(WILDCARD);
        return builder.toString();
    }


    /**
     * 是否是admin
     *
     * @param token
     * @return
     * @author kunbu
     * @time 2019/9/5 18:12
     **/
    public static boolean checkAdmin(String token) {
        if (token != null && token.length() > 0) {
            if (token.indexOf(TOKEN_PERSON_TYPE_ADMIN) >= 0) {
                return true;
            }
        }
        return false;
    }

}
