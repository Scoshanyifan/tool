package com.kunbu.common.util.basic.thread;

/**
 * @author kunbu
 * @date 2021/3/19 15:19
 **/
public class ThreadCommunicateTest {

    public static void main(String[] args) {

        testJoin();
    }

    public static void testJoin() {
        printCurrentThread("");

        Thread t1 = new Thread(new JoinRunnable(2000));
        Thread t2 = new Thread(new JoinRunnable(4000));
        Thread t3 = new Thread(new JoinRunnable(6000));
        t1.start();
        t2.start();
        t3.start();

        try {
            // 内部是调用Object中的wait方法实现线程阻塞
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 一直等待，直到【执行了join方法】的线程结束后，才会走到这
        printCurrentThread("等待join()结束");
    }

    public static void printCurrentThread(String description) {
        Thread currThread = Thread.currentThread();
        System.out.println(currThread.getName() + "-" + currThread.getState() + " " + description);
    }


    static class JoinRunnable implements Runnable {

        private long sleepTime;

        public JoinRunnable(long sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            ThreadCommunicateTest.printCurrentThread("start");
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadCommunicateTest.printCurrentThread("end");
        }
    }

}
