package com.kunbu.common.util.basic;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * bit和byte的转换
 * java byte = [-128,127]
 * 普通 byte = [0, 255]
 *
 * int: 20180713
 * 补码（计算机使用） 00000001 00110011 10010010 10010111
 * 原码 00000001 00110011 11101110 11101001
 * 反码 00000001 00110011 10010001 10010110
 *
 * 字节/进制的转换不建议直接使用Java的API，因为没有补齐
 * @see Integer#toString(int, int)
 * @see Integer#toBinaryString(int)
 * @see Integer#toHexString(int)
 *
 *
 * https://www.jianshu.com/p/058b46fef220
 */
public class ByteUtil {

    /**
     * 获取单字节第几位bit（低位算起）
     *
     * @param b 19（0001 0011）
     * @param index  2
     * @return 1
     */
    public static int getBitFromJavaByte(byte b, int index) {

        return (b >> (index - 1)) & 0x1;
    }

    /**
     * 单字节转8位二进制字符串
     *
     * @param b 19
     * @return "00010011"
     */
    public static String javaByte2BitString(byte b) {
        StringBuilder bitStr = new StringBuilder();
        return bitStr.append((b >> 7) & 0x1)
                .append((b >> 6) & 0x1)
                .append((b >> 5) & 0x1)
                .append((b >> 4) & 0x1)
                .append((b >> 3) & 0x1)
                .append((b >> 2) & 0x1)
                .append((b >> 1) & 0x1)
                .append((b >> 0) & 0x1).toString();
    }

    /**
     * 单字节转8位二进制数组
     *
     * @param b 19
     * @return [0, 0, 0, 1, 0, 0, 1, 1]
     */
    public static int[] javaByte2BitArray(byte b) {
        int[] bitArr = new int[8];
        for (int i = 7; i >= 0; i--) {
            //获取最低位
            bitArr[i] = (byte) (b & 0x1);
            //右移
            b = (byte) (b >> 1);
        }
        return bitArr;
    }

    /**
     * 有符号byte转无符号
     * @see Byte#toUnsignedInt(byte x)
     *
     * 字节流方法也是如此
     * ByteArrayInputStream中的read方法
     *  public synchronized int read() {
     * 		return (pos < count) ? (buf[pos++] & 0xff) : -1;
     *  }
     *
     * @param b -127
     * @return 129
     */
    public static int javaByte2Unsigned(byte b) {
        // 保证二进制补码一致性，将int高24位全部变成0，低8位保持不变（以下两种方式均可）

//        return (int) b & 0xff;
        return ((int) b) & 0xff;
    }

    /**
     * 无符号byte转有符号
     *
     * @param unsignedByte 129
     * @return -127
     */
    public static byte unsigned2javaByte(int unsignedByte) {
        return (byte)(unsignedByte - 256);
    }

    /**
     * java字节数组转二进制字符串
     *
     * @param byteArr [95, 15, -6, -20]
     * @param appendSpace
     * @return 01011111 00001111 11111010 11101100
     */
    public static String javaByteArr2BitString(byte[] byteArr, boolean appendSpace) {
        StringBuilder bitStr = new StringBuilder();
        for (byte b : byteArr) {
            bitStr.append(javaByte2BitString(b));
            if (appendSpace) {
                bitStr.append(" ");
            }
        }
        return bitStr.toString();
    }

    /**
     * 单字节转2位十六进制字符串（一个字节可表示为两个十六进制数字）
     *
     * @param -117
     * @return 8b(139)
     **/
    public static String javaByte2HexString(byte b, boolean upperCase) {
        //保证二进制补码一致性
        String hex = Integer.toHexString(b & 0xFF);
        //每个16进制共两位，若无高位就补0
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        if (upperCase) {
            return hex.toUpperCase();
        }
        return hex;
    }

    /**
     * java字节数组转十六进制字符串
     *
     * shift = 4
     * static int formatUnsignedInt(int val, int shift, char[] buf, int offset, int len) {
     * 		int charPos = len;
     * 		int radix = 1 << shift;	//16
     * 		int mask = radix - 1;
     * 		do {
     * 			buf[offset + --charPos] = Integer.digits[val & mask];
     * 			val >>>= shift;
     *        } while (val != 0 && charPos > 0);
     * 		return charPos;
     * }
     *
     * @param byteArr [95, 16, 3, -69]
     * @return 5f1003bb
     */
    public static String javaByteArr2HexString(byte[] byteArr, boolean upperCase) {
        StringBuilder hexStr = new StringBuilder();
        for (int i = 0; i < byteArr.length; i++) {
            String hv = Integer.toHexString(byteArr[i] & 0xFF);
            if (hv.length() < 2) {
                hexStr.append("0");
            }
            hexStr.append(hv);
        }
        if (upperCase) {
            return hexStr.toString().toUpperCase();
        }
        return hexStr.toString();
    }

    /**
     * java字节数组转无符号（用于转换成十六进制字符串）
     *
     * @param javaByteArr [95, 16, 5, -109]
     * @return [95, 16, 5, 147]
     */
    public static int[] javaByteArr2UnsignedByteArr(byte[] javaByteArr) {
        int[] unsignedArr = new int[javaByteArr.length];
        for (int i = 0; i < javaByteArr.length; i++) {
            if (javaByteArr[i] < 0) {
            }
            unsignedArr[i] = javaByteArr[i] & 0xFF;
        }
        return unsignedArr;
    }

    /**
     * 4位java字节数组转int
     *
     * @param byteArr [0, 0, 1, 21]
     * @return 277
     */
    public static int javaByteArr2Int(byte[] byteArr) {
        int res = 0;
        for (int i = 0; i < byteArr.length; i++) {
            res += (byteArr[i] & 0xff) << ((3 - i) * 8);
        }
        return res;
    }

    /**
     * 4位字节数组转int（低字节序）
     *
     * @param byteArr
     * @return
     */
    public static int javaByteArr2IntLow(byte[] byteArr) {
        int res = 0;
        for (int i = 0; i < byteArr.length; i++) {
            res += (byteArr[i] & 0xff) << (i * 8);
        }
        return res;
    }

    /**
     * int转16进制
     *
     * @param iv 284
     * @return 011c
     */
    public static String int2HexString(int iv, boolean upperCase) {
        String hexStr = Integer.toHexString(iv);
        if (hexStr.length() % 2 != 0) {
            hexStr = "0" + hexStr;
        }
        if (upperCase) {
            return hexStr.toUpperCase();
        }
        return hexStr;
    }

    /**
     * int转4位字节数组
     *
     * @param iv 1594885160
     * @return [95, 16, 4, 40]
     */
    public static byte[] int2JavaByteArr(int iv) {
        byte[] byteArr = new byte[4];
        byteArr[3] = (byte) (iv & 0xff);
        byteArr[2] = (byte) (iv >> 8 & 0xff);
        byteArr[1] = (byte) (iv >> 16 & 0xff);
        byteArr[0] = (byte) (iv >> 24 & 0xff);
        return byteArr;
    }

    /**
     * int转字节数组（低字节序）
     *
     * @param iv
     * @return
     */
    public static byte[] int2JavaByteArrLow(int iv) {
        byte[] byteArr = new byte[4];
        byteArr[0] = (byte) (iv & 0xff);
        byteArr[1] = (byte) (iv >> 8 & 0xff);
        byteArr[2] = (byte) (iv >> 16 & 0xff);
        byteArr[3] = (byte) (iv >> 24 & 0xff);
        return byteArr;
    }

    /**
     * int转无符号字节数组，用于转成16进制字符串
     *
     * @param intValue 1594885523
     * @return [95, 16, 5, 147]
     */
    public static int[] int2UnsignedByteArr(int intValue) {
        int[] unsignedArr = new int[4];
        for (int i = 0; i < 4; i++) {
            unsignedArr[4 - i - 1] = intValue >> i * 8 & 0xff;
        }
        return unsignedArr;
    }

    /**
     * int转16进制字符串数组
     *
     * @param intValue 1594885523
     * @return [AF, C9, 59, 5F]
     */
    public static String[] int2HexStringArr(int intValue, boolean upperCase) {
        String[] arrStr = new String[4];
        for (int i = 0; i < 4; i++) {
            String s = int2HexString(intValue >> i * 8 & 0xff, upperCase);
            arrStr[4 - i - 1] = s.toUpperCase();
        }
        return arrStr;
    }

    /**
     * 16进制字符串转int
     *
     * @param hexStr
     * @return
     */
    public static int hexString2Int(String hexStr) {

        return Integer.parseInt(hexStr, 16);
    }

    public static int[] hexString2IntArr(String hexStr) {
        int[] all = new int[hexStr.length() * 2];
        char[] crs = hexStr.toCharArray();
        for (int i = 0; i < crs.length; i++) {
            String hex = new String(new char[]{crs[i]});
            System.out.println(hex);
            if (hex.equals("0")) {
                all[i] = 0;
            } else {
                int b = Integer.parseInt(hex, 16) & 0xff;
                System.out.println(b);
                all[i] = b;
            }
        }
        System.out.println(Arrays.toString(all));
        return null;
    }

    public static void testJavaAPI() {
        int b = 284;
        System.out.println("API：" + Integer.toString(b, 2) + "/" + Integer.toBinaryString(b));
        System.out.println("API：" + Integer.toHexString(b));

        System.out.println(javaByte2BitString((byte) b));
        System.out.println(int2HexString(b, true));
        System.out.println();
    }

    public static void main(String[] args) {
        testJavaAPI();

        System.out.println("Integer.MAX_VALUE：" + Integer.MAX_VALUE);// 2147483647

        byte b = -127;
        System.out.println("字节低位起第n位：" + b + " >>> " + javaByte2BitString(b) + " >>> index:2 >>> " + getBitFromJavaByte(b, 2));
        System.out.println("字节的二进制表示：" + b + " >>> " + javaByte2BitString(b));
        System.out.println("字节的二进制数组：" + b + " >>> " + Arrays.toString(javaByte2BitArray(b)));
        System.out.println("字节转为无符号：" + b + " >>> " + javaByte2Unsigned(b));
        System.out.println();

        int i = 284;
        byte[] byteArr = int2JavaByteArr(i);
        System.out.println("int转字节数组：" + i + " >>> " + Arrays.toString(byteArr));
        System.out.println("字节数组转int：" + Arrays.toString(byteArr) + " >>> " + javaByteArr2Int(byteArr));
        System.out.println("int转16进制字符串：" + i + " >>> " + int2HexString(i, true));
        System.out.println("16进制字符串转int：" + int2HexString(i, true) + " >>> " + hexString2Int(int2HexString(i, true)));
        System.out.println("字节数组转二进制表示：" + Arrays.toString(byteArr) + " >>> " + javaByteArr2BitString(byteArr, true));
        System.out.println("字节转16进制字符串：" + -117 + " >>> " + javaByte2HexString((byte) -117, true));
        System.out.println("字节数组转16进制字符串：" + Arrays.toString(byteArr) + " >>> " + javaByteArr2HexString(byteArr, true));
        System.out.println();

        System.out.println(b + "的二进制形式（byte）：" + javaByte2BitString(b));
        System.out.println(b + "的二进制形式（int）：" + Integer.toBinaryString(b));
        System.out.println(b + " & 0xFF的十进制表示：" + (b & 0xff));
        System.out.println(b + " & 0xFF的二进制表示（高位省略了0）：" + Integer.toBinaryString(b & 0xff));
        System.out.println();

        //TODO 如果要和硬件直接交互，需要返回字节数组，即byte[]，至于换成int只是为了转成16进制字符串，最终mqtt底层扔出去的还是byte[]
        int intValue = (int) (System.currentTimeMillis() / 1000);
        byte[] signedArr = int2JavaByteArr(intValue);
        System.out.println("字节数组转无符号：" + Arrays.toString(signedArr) + " >>> " + Arrays.toString(javaByteArr2UnsignedByteArr(signedArr)));
        System.out.println("int转无符号字节数组：" + intValue + " >>> " + Arrays.toString(int2UnsignedByteArr(intValue)));
        System.out.println("int转16进制字符串数组：" + intValue + " >>> " + Arrays.toString(int2HexStringArr(intValue, true)));
        System.out.println();


        System.out.println(Integer.parseInt("1", 16));

        //0106f1737df5e9000000
        int intHex = hexString2Int("0106f1");
        System.out.println("intHex >>> " + intHex);
        System.out.println("intHex >>> " + Arrays.toString(int2UnsignedByteArr(intHex)));
        System.out.println("intHex >>> " + Arrays.toString(int2JavaByteArr(intHex)));

//        hexString2IntArr("0106f1737df5e9000000");



        System.out.println(unsigned2javaByte(129));
    }

}
