package com.kunbu.common.util.basic.test;

/**
 * @author kunbu
 * @date 2020/10/26 14:28
 **/
public class IfTest {

    public static void testLogistic() {

        int status = 3;
        // 和SQL中的where表达式不要混淆，where status != 0 or status !=1 中条件是独立的（逻辑运算中是关联的）
        if (status != 0 || status != 1) {
            System.out.println("1");
        }
        if (status != 0 && status != 1) {
            System.out.println("2");
        }

        int num = 7;
        if (num < 10 || num++ > 10) {
            System.out.println("3:" + num);
        }
        // 表示多个条件都需要判断，即便有表达式返回true,剩余条件仍需要判断
        if (num < 10 | num++ > 10) {
            System.out.println("4:" + num);
        }

        int age = 23;
        if (age > 30 && age-- < 30 ) {
            System.out.println("5:" + age);
        } else {
            System.out.println("5-2:" + age);
        }
        // 如果在多个表达式中有条件返回了false，剩余的条件也要判断
        if (age > 30 & age-- < 30 ) {
            System.out.println("6:" + age);
        } else {
            System.out.println("6-2:" + age);
        }
    }

    public static void main(String[] args) {
        testLogistic();
    }
}
