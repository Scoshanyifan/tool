package com.kunbu.common.util.basic.thread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 *
 * @author kunbu
 * @date 2021/3/15 10:02
 **/
public class ThreadLockTest {

    public static void main(String[] args) {

        LoopLock lock = new LoopLock();
        lock.lock();
        new Thread(() -> {
            lock.lock();
            System.out.println("sub get lock");
            lock.unlock();
        }).start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

}

class LoopLock {

    private AtomicReference<Thread> cas = new AtomicReference<>();

    public boolean lock() {
        Thread currentThread = Thread.currentThread();
        // 如果old是null，期望是null，则说明抢锁成功，反之一直循环抢锁
        while (!cas.compareAndSet(null, currentThread)) {
            System.out.println(currentThread.getName() + " >>> lock failure");
        }
        System.out.println(currentThread.getName() + " lock success");
        return true;
    }

    public boolean unlock() {
        Thread currentThread = Thread.currentThread();
        System.out.println(currentThread.getName() + " unlock");
        return cas.compareAndSet(currentThread, null);
    }
}