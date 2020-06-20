package com.kunbu.common.util.basic.test;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 字符串连接器：能够按指定分割符分隔，并且有前后缀
 *  1. StringBuilder
 *  2. StringJoiner
 *  3. Collectors.joining
 *
 * 字符串分隔
 *  1. String.split
 *  2. Splitter
 *
 * 字符串格式化
 *  1. String.format
 *  2. MessageFormat.format
 *
 *
 * @author: kunbu
 * @create: 2019-11-21 13:52
 **/
public class StringTest {

    private static final List<String> AREA_LIST = Lists.newArrayList("广州", "上海", "杭州", "宁波");

    private static final String SPLITTER	= ",";
    private static final String PREFIX      = "{";
    private static final String SUFFIX	  	= "}";


    /**
     * 默认创建16位char数组value用于保存数据
     * 底层调用：System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
     *
     **/
    public static String testStringBuilder(List<String> items, String delimiter, String prefix, String suffix) {
        StringBuilder builder = new StringBuilder(prefix);
        if (!CollectionUtils.isEmpty(items)) {
            items.stream().forEach(x -> builder.append(x).append(delimiter));
            //去除最后一个逗号
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.append(suffix).toString();
    }

    public static String testStringJoiner(List<String> items, String delimiter, String prefix, String suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        if (!CollectionUtils.isEmpty(items)) {
            items.stream().forEach(x -> joiner.add(x));
        }
        return joiner.toString();
    }

    /**
     * 内部通过SpringJoiner实现
     *
     **/
    public static String testCollectorsJoining(List<String> items, String delimiter, String prefix, String suffix) {
        if (!CollectionUtils.isEmpty(items)) {
            return items.stream()
                    .collect(Collectors.joining(delimiter, prefix, suffix));
        } else {
            return prefix + suffix;
        }
    }


    public static String testIndexOfSubstring(String original, String indexStr) {
        int start = original.indexOf(indexStr);
        if (start >= 0) {
            start += indexStr.length();
            int end = original.indexOf("\n", start);
            if (end >= 0) {
                return original.substring(start, end);
            } else {
                return original.substring(start);
            }
        }
        return original;
    }


    /**
     * 如果是：//// 然后使用String.split，会有bug，不能正确划分，会出现空数据情况（改用guava的Splitter）
     *
     **/
    public static void testSplit(String historyTemp) {
        //String.split()
        String[] tempStrArr = historyTemp.split("/");
        System.out.println("String.split(): " + Arrays.toString(tempStrArr));

    }

    public static void testGuavaSplitter(String str) {
        //guava.Splitter
        List<String> tempStrList = Splitter.on("/").splitToList(str);
        System.out.println("Splitter: " + tempStrList);
    }


    public static void testReplace() {
        String workTime = "12:23-14:31";
        String executeTime = workTime.replace(":", "");
        System.out.println(executeTime);
    }


    /**
     * 字符串：%s（%[idx]$[m]s，[idx]表示用第一个参数填充，[m]表示多少个空格补齐）
     * 浮点数：%f（%[idx]$[标识][最少宽度][.精度]转换方式）
     *
     * https://blog.csdn.net/yaerfeng/article/details/7328092
     **/
    public static void testStringFormat() {
        /**
         * '-'    在最小宽度内左对齐，不可以与“用0填充”同时使用
         * '#'    只适用于8进制和16进制，8进制时在结果前面增加一个0，16进制时在结果前面增加0x
         * '+'    结果总是包括一个符号（一般情况下只适用于10进制，若对象为BigInteger才可以用于8进制和16进制）
         * ' '    正值前加空格，负值前加负号（一般情况下只适用于10进制，若对象为BigInteger才可以用于8进制和16进制）
         * '0'    结果将用零来填充
         * ','    只适用于10进制，每3位数字之间用“，”分隔
         * '('    若参数是负数，则结果中不添加负号而是用圆括号把数字括起来（同‘+’具有同样的限制）
         *
         **/
        System.out.println("num：" + String.format("%06d", -233));
        System.out.println("num：" + String.format("%6d", -233));
        System.out.println("num：" + String.format("%-6d", -233));
        System.out.println("num：" + String.format("%#6x", 5689));
        // 4s表示4空格补齐
        String msg = String.format("用户%s，收到打款%2$.2f，收款人：%1$4s", "李四", 1024.6);
        System.out.println("msg: " + msg);
    }

    /**
     * 单引号''用于忽略特定占位符，比如左花括号{，必须成对出现
     *
     * https://blog.csdn.net/qq_36538061/article/details/78506758
     **/
    public static void testMessageFormat() {
        String format = "这里展示不同数据类型[{0}]，[{1}]，[{2}]，['{3}']，[{4}]，[{5}]的区别";
        // [string]，[a]，[null]，[{3}]，[999]，[{5}]
        Object[] params = {"string", 'a', null, 988, 999};
        String message =  MessageFormat.format(format, params);
        System.out.println("message: " + message);

        String pattern = "单引号用于忽略特定符号，如左花括号：'{'-}，而双引号需要转义：\"";
        // MessageFormat.format静态方法每次回创建一个对象，所以为了复用可以用 new MessageFormat(message)
        MessageFormat mf = new MessageFormat(pattern);
        String messageRes = mf.format(null);
        System.out.println("messageRes: " + messageRes);
    }


    /**
     * 测试交集并集等
     *
     **/
    public static void testGroup() {
//        Set<String> result1 = Sets.union(set1, set2);//合集，并集
//        Set<String> result2 = Sets.intersection(set1, set2);//交集
//        Set<String> result3 = Sets.difference(set1, set2);//差集 1中有而2中没有的
//        Set<String> result4 = Sets.symmetricDifference(set1, set2);//相对差集 1中有2中没有  2中有1中没有的 取出来做结果
    }



    public static void main(String[] args) {

        System.out.println(testStringBuilder(AREA_LIST, SPLITTER, PREFIX, SUFFIX));
        System.out.println(testStringJoiner(AREA_LIST, SPLITTER, PREFIX, SUFFIX));
        System.out.println(testCollectorsJoining(null, SPLITTER, PREFIX, SUFFIX));


        testSplit("////");
        testSplit("2/-5///");
        testGuavaSplitter("////");
        testGuavaSplitter("2/-5///");

        System.out.println();
        testStringFormat();

        System.out.println();
        testMessageFormat();

        System.out.println();
        System.out.println(testIndexOfSubstring("SHi-110，资产编号:123456\n，结束", "资产编号:"));

        testReplace();

        System.out.println("num：" + String.format("%03d", Integer.parseInt("23")));

    }
}
