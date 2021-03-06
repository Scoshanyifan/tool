package com.kunbu.common.util.basic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-09 10:02
 **/
public class MatchUtil {

    /** 车牌号 */
    public static final Pattern REGEX_CARNO = Pattern.compile("^[A-Z][A-HJ-NP-Z\\d]{5}$");
    /** 身份证 */
    private static final Pattern REGEX_IDCARD = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
    /** 新能源车牌号 */
    public static final Pattern REGEX_NEWCAR = Pattern.compile("^[A-Z][D,F][\\d]{5}$");
    /** 手机号 */
    public static final Pattern REGEX_MOBILE_PHONE = Pattern.compile("^1(3|4|5|7|8)\\d{9}$");
    /** 回车空格制表换行 */
    public static final Pattern REGEX_WHITE_BLANK = Pattern.compile("\"\\\\s*|\\t|\\r|\\n\"");
    /** 国内固定电话 */
    public static final Pattern REGEX_SOLID_INNER_PHONE = Pattern.compile("^(0\\d{2,3})\\-([1-9]\\d{6,7})$");

    /** 1-500数字 */
    public static final Pattern REGEX_BETWEEN_NUM = Pattern.compile("^(500|[1-4]{0,1}\\d{0,1}\\d)$");

    /** 手机和邮箱 */
    public static final Pattern REGEX_PHONE_EMAIL = Pattern.compile("^(1[3456789]\\d{9})|([a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+)$");
    public static final Pattern REGEX_VERSION = Pattern.compile("^(\\d\\.\\d\\.\\d)(\\_[\\d]{1,3})?$");

    /**
     * 0-9 a-z A-Z
     */
    private static final int ASCII_NUMBER_START = 48;
    private static final int ASCII_NUMBER_END = 57;
    private static final int ASCII_UPCASE_EN_START = 65;
    private static final int ASCII_UPCASE_EN_END = 90;
    private static final int ASCII_LOWCASE_EN_START = 97;
    private static final int ASCII_LOWCASE_EN_END = 122;

    /**
     * 中文（2位char）
     */
    private static final int CHAR_CN_START = 19968;
    private static final int CHAR_CN_END = 40869;

    /**
     * 替换空白符
     * \n 回车(\u000a) \s 空格(\u0008) \r 换行(\u000d) \t 水平制表符(\u0009)
     *
     * @param str
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Matcher m = REGEX_WHITE_BLANK.matcher(str);
            return m.replaceAll("");
        }
        return "";
    }

    /**
     * 验证正则
     *
     * @param str
     * @param regex
     */
    public static boolean checkRegex(String str, String regex) {
        if (str.matches(regex)) {
            return true;
        }
        return false;
    }

    /**
     * 验证正则
     *
     * @param str
     * @param regex
     */
    public static boolean checkRegex(String str, Pattern regex) {
        Matcher m = regex.matcher(str);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否只包含中文或英文
     *
     * @param str
     */
    public static boolean checkChineseEnglish(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            int n = str.charAt(i);
            if (n >= ASCII_UPCASE_EN_START && n <= ASCII_UPCASE_EN_END) {
                continue;
            }
            if (n >= ASCII_LOWCASE_EN_START && n <= ASCII_LOWCASE_EN_END) {
                continue;
            }
            if (n >= CHAR_CN_START && n < CHAR_CN_END) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 检查字符串长度(中文,空格算2个)
     *
     * 字符编码不同，长度不同
     *
     * @param in
     * @param limitDown
     * @param limitUp
     * @return
     */
    @Deprecated
    public static boolean checkStringCount(String in, int limitDown, int limitUp) {
        if (in == null || in.isEmpty()) {
            return false;
        }
        int size = 0;
        for (int i = 0; i < in.length(); i++) {
            int n = in.charAt(i);
            if (CHAR_CN_START <= n && n < CHAR_CN_END) {
                //中文+2
                size += 2;
            } else if (n == 32) {
                //空格+2
                size += 2;
            } else {
                size += 1;
            }
        }
        if (size <= limitUp && size >= limitDown) {
            return true;
        }
        return false;
    }

    /**
     * 分组
     */
    public static void testGroup() {

        Matcher m = REGEX_SOLID_INNER_PHONE.matcher("010-12345678");
        if (m.matches()) {
            System.out.println("group count: " + m.groupCount());
            // 等价于group(0)，即返回全部
            System.out.println("group : " + m.group());
            System.out.println("group 1: " + m.group(1));
        }

    }

    public static void main(String[] args) {

        testGroup();

        String name = " 一二三四五六七八九十 一十二十三十四  ".trim();
        if (!checkStringCount(name, 8, 36)) {
            System.out.println("chaochu;");
        }

        String dotRegex = ",";
        System.out.println("check: " + checkRegex("B1,1", Pattern.compile(dotRegex)));

        /** 此正则识别,到:之间的所有ascii值，而需求是只匹配,/:/-三中符号 */
        Pattern p = Pattern.compile("[0-9a-zA-Z,-:]+");
        Pattern p2 = Pattern.compile("[0-9a-zA-Z,:\\-]+");
        boolean b = p.matcher("1.1:2.1").matches();
        boolean b2 = p.matcher("-1:2").matches();
        System.out.println(b);
        System.out.println(b2);

        String sn = "dsade111";
        if (!sn.matches("^[A-Za-z0-9]+$")) {
            System.out.print("failure");
        }

        System.out.println("check phone and email: " + checkRegex("1880990009xx", REGEX_PHONE_EMAIL));
        System.out.println("version: " + checkRegex("1.0.2_23", REGEX_VERSION));
    }

}
