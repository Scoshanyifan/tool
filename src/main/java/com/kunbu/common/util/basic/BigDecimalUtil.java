package com.kunbu.common.util.basic;

import java.math.BigDecimal;

/**
 * 精确浮点数计算工具类
 * http://blog.csdn.net/stevene/article/details/586089
 *
 * PS: double的构造有不可预知性，所以这里需要用string类型的构造参数，如果可以是long或int当然更好
 *
 * 8种取舍方式：
 * ROUND_CEILING: 		向正无穷舍入 		2.36 >>> 2.4   -2.36 >>> -2.3
 * ROUND_FLOOR: 		向负无穷舍入 		2.36 >>> 2.3   -2.36 >>> -2.4
 * ROUND_UP: 			远离0方向舍入		2.36 >>> 2.4   -2.36 >>> -2.4
 * ROUND_DOWN: 			向0方向舍入		2.36 >>> 2.3   -2.36 >>> -2.3
 *
 * ROUND_HALF_DOWN:     五舍六入
 * ROUND_HALF_UP:       四舍五入（常用）
 *
 * ROUND_HALF_EVEN:     不建议使用
 * ROUND_UNNECESSARY:
 *
 * @author kunbu
 * @time 2019/11/22 17:30
 **/
public class BigDecimalUtil {

    /** 小数点后保留位数，默认2位 */
    private static final int DEFAULT_SCALE = 2;
    /** 默认取舍方式：四舍五入 */
    private static final int DEFAULT_ROUND_MODE = BigDecimal.ROUND_HALF_UP;
    /** 8种取舍方式 */
    private static final int[] ROUND_MODE_ARRAY = {BigDecimal.ROUND_UP, BigDecimal.ROUND_DOWN, BigDecimal.ROUND_FLOOR,
            BigDecimal.ROUND_HALF_UP, BigDecimal.ROUND_HALF_DOWN, BigDecimal.ROUND_HALF_EVEN, BigDecimal.ROUND_UNNECESSARY};

    /**
     * 加
     * @param value1
     * @param value2
     * @return
     */
    public static double add(double value1, double value2) {
        return add(value1, value2, DEFAULT_SCALE);
    }

    public static double add(double value1, double value2, int scale) {
        return add(value1, value2, scale, DEFAULT_ROUND_MODE);
    }

    public static double add(double value1, double value2, int scale, int roundMode) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        if (scale < 0) {
            scale = DEFAULT_SCALE;
        }
        if (!checkRoundMode(roundMode)) {
            roundMode = DEFAULT_ROUND_MODE;
        }
        return b1.add(b2).setScale(scale, roundMode).doubleValue();
    }

    /**
     * 减
     * @param value1
     * @param value2
     * @return
     */
    public static double sub(double value1, double value2) {
        return sub(value1, value2, DEFAULT_SCALE);
    }

    public static double sub(double value1, double value2, int scale) {
        return sub(value1, value2, scale, DEFAULT_ROUND_MODE);
    }

    public static double sub(double value1, double value2, int scale, int roundMode) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        if (scale < 0) {
            scale = DEFAULT_SCALE;
        }
        if (!checkRoundMode(roundMode)) {
            roundMode = DEFAULT_ROUND_MODE;
        }
        return b1.subtract(b2).setScale(scale, roundMode).doubleValue();
    }

    /**
     * 乘
     * @param value1
     * @param value2
     * @return
     */
    public static double mul(double value1, double value2) {
        return mul(value1, value2, DEFAULT_SCALE);
    }

    public static double mul(double value1, double value2, int scale) {
        return mul(value1, value2, scale, DEFAULT_ROUND_MODE);
    }

    public static double mul(double value1, double value2, int scale, int roundMode) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        if (scale < 0) {
            scale = DEFAULT_SCALE;
        }
        if (!checkRoundMode(roundMode)) {
            roundMode = DEFAULT_ROUND_MODE;
        }
        return b1.multiply(b2).setScale(scale, roundMode).doubleValue();
    }

    /**
     * 除
     * @param value1
     * @param value2
     * @return
     */
    public static double div(double value1, double value2) {
        return div(value1, value2, DEFAULT_SCALE);
    }

    public static double div(double value1, double value2, int scale) {
        return div(value1, value2, scale, DEFAULT_ROUND_MODE);
    }

    public static double div(double value1, double value2, int scale, int roundMode) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        if (scale < 0) {
            scale = DEFAULT_SCALE;
        }
        if (!checkRoundMode(roundMode)) {
            roundMode = DEFAULT_ROUND_MODE;
        }
        return b1.divide(b2, scale, roundMode).doubleValue();
    }

    /**
     * 保留2位小数的浮点数： 150/10, 3 >>> 15.000
     *
     * ps: 如果用div返回double，上面的结果只能是15.0
     *
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    public static String div2Str(double value1, double value2, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.divide(b2, scale, DEFAULT_ROUND_MODE).toString();
    }

    /**
     * 整形取小数位，常用于金额： point=2, 2000 >>> 20.00
     * @param val
     * @param point
     * @return
     */
    public static String movePoint(long val, int point) {
        if (point < 0) {
            point = DEFAULT_SCALE;
        }
        return new BigDecimal(val).movePointLeft(point).toString();
    }

    /**
     * 四舍五入到指定小数位
     *
     * <p> scale=1, 4.333 >>> 4.3
     * <p> scale=2, 3.758 >>> 3.76
     * @param original 浮点数
     * @param scale 小数位数
     */
    public static double retainDecimal(double original, int scale) {
        return retainDecimal(original, scale, DEFAULT_ROUND_MODE);
    }

    /**
     * 四舍五入到指定小数位，和不同的舍入方式
     *
     * @param original
     * @param scale
     * @param roundMode
     **/
    public static double retainDecimal(double original, int scale, int roundMode) {
        if (scale < 0) {
            scale = DEFAULT_SCALE;
        }
        if (!checkRoundMode(roundMode)) {
            roundMode = DEFAULT_ROUND_MODE;
        }
        BigDecimal b = new BigDecimal(Double.toString(original));
        return b.setScale(scale, roundMode).doubleValue();
    }

    private static boolean checkRoundMode(int roundMode) {
        for (int rm : ROUND_MODE_ARRAY) {
            if (roundMode == rm) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

        System.out.println("四舍五入：" + retainDecimal(3.755, 2));
        System.out.println("五舍六入：" + retainDecimal(3.755, 2, BigDecimal.ROUND_HALF_DOWN));
        System.out.println("加：" + add(2.1, 3.333));
        System.out.println("减" + sub(27.3, 2.34));
        System.out.println("乘" + mul(2.3, 7.34));
        System.out.println("整数除" + div(2, 3, 2));

        System.out.println("小数除" + div(20.00, 0.01, 0));
        System.out.println("除法返回double" + div(1500, 100, 3));
        // 如果要15.000这种效果，把doubleValue()改成toString()
        System.out.println("除法返回string" + div2Str(1500, 100, 3));

        System.out.println("分转元：" + movePoint(2000, 2));

        String n = "1524156781705602397";
        BigDecimal ll = new BigDecimal(n);
        // -6922976412290386228
        System.out.println("num size: " + n.length() + " bd: " + ll.longValue());
    }

}
