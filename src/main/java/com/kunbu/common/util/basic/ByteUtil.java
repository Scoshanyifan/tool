package com.kunbu.common.util.basic;

import java.util.Arrays;

/**
 * bit和 byte的转换
 * java byte = [-128,127]
 * 普通byte = [0, 255]
 *
 * @see Byte
 * @author scosyf
 *
 * https://www.jianshu.com/p/058b46fef220
 */
public class ByteUtil {

    /**
     * 获取字节第几位（低位算起）
     * @param origin
     * @param index
     * @return
     */
    public static int getBit(byte origin, int index) {
        return (origin >> index) & 0x1;
    }

    /**
     * 获取字节的二进制数组
     * @param origin
     * @return
     */
    public static byte[] getBitArray(byte origin) {
        byte[] bitArr = new byte[8];
        for (int i = 7; i >= 0; i--) {
            //获取最低位
            bitArr[i] = (byte) (origin & 0x1);
            //右移
            origin = (byte) (origin >> 1);
        }
        return bitArr;
    }

    /**
     * 获取字节的8位二进制表示
     * @param origin
     * @return
     */
    public static String getBitString(byte origin) {
        StringBuilder bitStr = new StringBuilder();
        return bitStr.append((origin >> 7) & 0x1)
                .append((origin >> 6) & 0x1)
                .append((origin >> 5) & 0x1)
                .append((origin >> 4) & 0x1)
                .append((origin >> 3) & 0x1)
                .append((origin >> 2) & 0x1)
                .append((origin >> 1) & 0x1)
                .append((origin >> 0) & 0x1).toString();
    }

    /**
     * 获取字节二进制简化表示
     * @param origin
     * @return
     */
    public static String getBitStringSimple(byte origin) {

        return Integer.toString(origin, 2);
    }

    /**
     * 有符号byte转成无符号
     * @param origin
     * @return
     *
     * @see Byte#toUnsignedInt(byte x)
     */
    public static int toUnsignedInt(byte origin) {
        /**
         * ByteArrayInputStream中的read方法
         * public synchronized int read() {
         * 		return (pos < count) ? (buf[pos++] & 0xff) : -1;
         * }
         */
        //将高24位全部变成0，低8位保持不变
        return ((int) origin) & 0xff;
    }

    /**
     * 字节数组转16进制
     * @param origin
     * @return
     */
    public static String byteToHexString(byte[] origin) {
        StringBuilder hexStr = new StringBuilder();
        if (origin == null || origin.length <= 0) {
            return null;
        }
        for (int i = 0; i < origin.length; i++) {
            //保证二进制补码一致性
            int iv = origin[i] & 0xFF;

            /**
             * shift = 4
             * static int formatUnsignedInt(int val, int shift, char[] buf, int offset, int len) {
             * 		int charPos = len;
             * 		int radix = 1 << shift;	//16
             * 		int mask = radix - 1;
             * 		do {
             * 			buf[offset + --charPos] = Integer.digits[val & mask];
             * 			val >>>= shift;
             * 		} while (val != 0 && charPos > 0);
             * 		return charPos;
             * }
             */
            String hv = Integer.toHexString(iv);
            //每个16进制共两位，若无高位就补0
            if (hv.length() < 2) {
                hexStr.append(0);
            }
            hexStr.append(hv);
        }
        return hexStr.toString();
    }

    public static String int2HexString(int iv) {
        String hexStr = Integer.toHexString(iv);
        if (hexStr.length() < 2) {
            hexStr = "0" + hexStr;
        }
        return hexStr;
    }

    public static int hexString2Int(String hexStr) {

        return Integer.parseInt(hexStr, 16);

    }

    public static String hexString2Binary(String hexStr) {
        int i = Integer.parseInt(hexStr, 16);
        String str2 = Integer.toBinaryString(i);
        int zero = 8 - str2.length();
        if (zero > 0) {
            for (int idx = 0; idx < zero; idx++) {
                str2 = "0" + str2;
            }
        }
        return str2;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getBitArray((byte) -1)));
        System.out.println(getBitString((byte) -127));
        System.out.println(getBit((byte) 7, 2));
        System.out.println(getBitStringSimple((byte) 5));
        System.out.println(toUnsignedInt((byte) -127));

        byte b1 = -127;
        int i1 = b1;
        int i2 = b1 & 0xFF;
        System.out.println("(byte)-127的二进制形式：" + getBitString(b1));
        System.out.println("(int)-127的二进制形式：" + Integer.toBinaryString(i1));
        System.out.println("-127 & 0xFF的十进制形式：" + i2);
        //高位省略了0
        System.out.println("-127 & 0xFF的二进制形式：" + Integer.toBinaryString(i2));

        byte[] hbs = new byte[]{17,2,7,111,-127};
        System.out.println(byteToHexString(hbs));


        System.out.println(hexString2Binary("20"));
    }

}
