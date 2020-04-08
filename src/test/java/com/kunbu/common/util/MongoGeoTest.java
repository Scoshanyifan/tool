package com.kunbu.common.util;

import com.google.common.collect.Lists;
import com.kunbu.common.util.tool.sql.mongo.demo.entity.DeviceLocation;
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
public class MongoGeoTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoReadTest.class);

    private static final double latitude        = 29.88821770294548;
    private static final double longitude       = 121.54937256787107;
    // 0.01 =1km
    private static final double maxDistanceKM   = 1;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void initData() {
        Random random = new Random();
        List<DeviceLocation> locations = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            DeviceLocation location = new DeviceLocation();
            location.setDeviceSn(random.nextInt(10000) + "-" + i);
            location.setLocation(
                    new GeoJsonPoint(longitude + random.nextInt(100) / 10000d, latitude + random.nextInt(100) / 10000d));
            locations.add(location);
        }
        mongoTemplate.insert(locations, DeviceLocation.class);
    }

    /**
     * 查询目标点周围一定距离内的所有点
     *
     **/
    @Test
    public void testMaxDistance() {

        NearQuery nearQuery = NearQuery
                .near(longitude, latitude, Metrics.KILOMETERS)
                .maxDistance(maxDistanceKM);
        GeoResults<DeviceLocation> geoResults = mongoTemplate.geoNear(nearQuery, DeviceLocation.class);
        Iterator<GeoResult<DeviceLocation>> iterator = geoResults.iterator();
        while (iterator.hasNext()) {
            GeoResult<DeviceLocation> geo = iterator.next();
            DeviceLocation dl = geo.getContent();
            logger.info(">>> distance:{}, device location:{}", geo.getDistance().getValue(), dl);
        }
    }

    /**
     * 查询目标点周围最近的点
     *
     **/
    @Test
    public void testOrderByDistance() {

        Query locationQuery = new Query(Criteria.where("location").nearSphere(new Point(longitude, latitude))).limit(5);
        List<DeviceLocation> deviceLocations = mongoTemplate.find(locationQuery, DeviceLocation.class);
        for (DeviceLocation dl : deviceLocations) {
            logger.info(">>> device location:{}", dl);
        }

    }

}
