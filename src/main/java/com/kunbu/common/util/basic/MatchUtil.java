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
    public static final Pattern REGEX_PHONE = Pattern.compile("^1(3|4|5|7|8)\\d{9}$");
    /**  回车空格制表换行 */
    public static final Pattern REGEX_WHITE_BLANK = Pattern.compile("\"\\\\s*|\\t|\\r|\\n\"");

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

    public static void main(String[] args) {
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
    }

}
