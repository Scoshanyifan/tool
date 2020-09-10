package com.kunbu.common.util.redis;

import com.google.common.collect.Lists;
import com.kunbu.common.util.mongo.MongoReadTest;
import com.kunbu.common.util.tool.sql.mongo.common.DeviceLocation;
import com.kunbu.common.util.tool.sql.redis.RedisManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-02 16:43
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoReadTest.class);

    @Autowired
    private RedisManager redisManager;


    /**
     * 测试大文件存储时，引起的阻塞
     */
    @Test
    public void testSaveBigFile() {
        File file = new File("");
    }


}
