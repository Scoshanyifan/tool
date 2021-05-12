package com.kunbu.common.util.mysql;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kunbu.common.util.basic.ExecutorUtil;
import com.kunbu.common.util.mongo.MongoReadTest;
import com.kunbu.common.util.tool.sql.mysql.mybatis.CitizenBean;
import com.kunbu.common.util.tool.sql.mysql.mybatis.biz.CitizenMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * TODO 模拟并发写操作
 *
 *
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestConcurrency {

    private static final Logger logger = LoggerFactory.getLogger(TestConcurrency.class);

    @Autowired
    private CitizenMapper commonMapper;

    @Test
    public void testThread() throws Exception {

        CitizenBean citizenBean = new CitizenBean();
        citizenBean.setAccount("account");
        citizenBean.setAddress("address");
        citizenBean.setAge(233);
        citizenBean.setCity("city");
        citizenBean.setCreateTime(new Date());
        citizenBean.setIdCard("idcard");
        citizenBean.setName("name");
        citizenBean.setPhone("phone");

        ExecutorService pool = ExecutorUtil.generatePool("test", 20, 20);
        for (int i = 0; i < 50; i++) {
//            pool.execute(new AtomicRunnable(citizenBean));
            pool.execute(new NonAtomicRunnable(citizenBean));
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        System.out.println("test over");
    }

    class AtomicRunnable implements Runnable {

        private CitizenBean citizenBean;

        AtomicRunnable(CitizenBean citizenBean) {
            this.citizenBean = citizenBean;
        }

        @Override
        public void run() {
            int delRes = commonMapper.deleteByCondition(citizenBean.getIdCard(), citizenBean.getAccount());
            System.out.println(System.currentTimeMillis() + " [" + Thread.currentThread().getName() + "] - atomic delRes: " + delRes);
            int count = commonMapper.countByCondition(citizenBean.getIdCard(), citizenBean.getAccount());
            System.out.println(System.currentTimeMillis() + " [" + Thread.currentThread().getName() + "] - atomic count: " + count);
            int saveRes = commonMapper.insertByNotExistsNormalField(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                    citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
            System.out.println(System.currentTimeMillis() + " [" + Thread.currentThread().getName() + "] - atomic saveRes: " + saveRes);
        }
    }

    class NonAtomicRunnable implements Runnable {

        private CitizenBean citizenBean;

        NonAtomicRunnable(CitizenBean citizenBean) {
            this.citizenBean = citizenBean;
        }

        @Override
        public void run() {
            int delRes = commonMapper.deleteByCondition(citizenBean.getIdCard(), citizenBean.getAccount());
            System.out.println(System.currentTimeMillis() + " [" + Thread.currentThread().getName() + "] - non-atomic delRes: " + delRes);
            int count = commonMapper.countByCondition(citizenBean.getIdCard(), citizenBean.getAccount());
            System.out.println(System.currentTimeMillis() + " [" + Thread.currentThread().getName() + "] - non-atomic count: " + count);
            if (count <= 0) {
                int res = commonMapper.insertOne(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                System.out.println(System.currentTimeMillis() + " [" + Thread.currentThread().getName() + "] - non-atomic res: " + res);
            }
        }
    }
}
