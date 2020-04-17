package com.kunbu.common.util.basic.test;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.Arrays;

/**
 *          Throwable
 *      ________|__________
 *      |                 |
 *    Error             exception
 *                  ________|________
 *                  |               |
 *             IOException...     RuntimeException
 *             受检异常            运行时异常（非受检异常）
 *
 * tips：
 * 1.在catch中，非受检异常的抛出（业务相关等等），需要 throw new RuntimeException(e);
 * 2.日志记录异常需要直接扔e，即log.fail("..." + e)
 *
 * ps：上述方法在正常情况下可以打印出错误信息，但是有时候不能展示堆栈信息，需要手动用流获取再打印
 *
 * 关于finally，参见 system >>> keyword
 */
public class ExceptionTest {

    /**
     * 异常捕获的另一种写法：更优雅的方式来实现资源的自动释放，不然finally中也需捕获异常
     *
     * try代码块退出时，会自动调用reader.close方法，若close抛出异常时会被抑制，仍然抛出原先的异常
     */
    public static void testTryResource() {
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("hello")))) {
            //
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 受检异常
     */
    public static void testException(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("hello")));
            //
        } catch (FileNotFoundException e) {
            System.out.println(">>> e:\n" + e);
            System.out.println(">>> e.getCause():\n" + e.getCause());
            System.out.println(">>> e.getMessage():\n" + e.getMessage());
            System.out.println(">>> e.getLocalizedMessage():\n" + e.getLocalizedMessage());
            System.out.println(">>> e.getClass():\n" + e.getClass());
            System.out.println(">>> e.getStackTrace():\n" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(reader);
        }
    }

    /**
     * 非受检异常
     */
    public static void testRuntimeException() {
        try {
            Lists.newArrayList().get(0);
        } catch (Exception e) {
            //1.推荐这种写法，将异常信息包装起来再交给log打印
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            e.printStackTrace(new PrintStream(bos));
            System.out.println(">>> 手动打印 e 方法一:\n" + bos.toString());

            //2.
            String sOut = "";
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                sOut += "\tat " + s + "\r\n";
            }
            System.out.println(">>> 手动打印 e 方法二:\n" + sOut);

            //3.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            System.out.println(">>> 手动打印 e 方法三:\n" + sOut);

            //可以看出，一般情况下，手动打印和直接抛e都可以展示堆栈信息
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

//        testTryResource();
//        testException();
        testRuntimeException();
    }
}
