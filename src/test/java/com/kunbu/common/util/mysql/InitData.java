package com.kunbu.common.util.mysql;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kunbu.common.util.mongo.MongoReadTest;
import com.kunbu.common.util.tool.sql.mysql.CommonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitData {

    private static final Logger logger = LoggerFactory.getLogger(MongoReadTest.class);

    private static final String[] cityArr = {"上海", "济南", "海口", "天津", "宁波", "武汉", "成都",
            "温州", "长春", "舟山", "南京", "扬州", "厦门", "德清", "广州", "深圳", "常德", "贵阳"};

    private static final String[] nameArr = {"赵", "钱", "孙", "李", "周", "吴", "郑", "冯", "陈", "褚",
            "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施", "张"};

    private static final String[] detailArr = {"苹果", "香蕉", "芒果", "橙子", "牛油果", "蜜桃", "西瓜",
            "蓝莓", "橘子", "火龙果", "椰子", "草莓"};

    @Autowired
    private CommonMapper commonMapper;

    @Test
    public void initData() {
        long time = new Date().getTime();
        Random r = new Random();
        for (int loop = 1; loop <= 100; loop++) {
            logger.info(">>> init data, loop:{}", loop);
            List<Map<String, Object>> citizenList = Lists.newArrayList();
            for (int i = 1; i <= 1000_0; i++) {
                Map<String, Object> c = Maps.newHashMap();
                c.put("idCard", generateIdCard());
                c.put("city", cityArr[r.nextInt(cityArr.length)]);
                c.put("account", UUID.randomUUID().toString().replace("-", ""));
                c.put("name", nameArr[r.nextInt(nameArr.length)] + nameArr[r.nextInt(nameArr.length)] + nameArr[r.nextInt(nameArr.length)]);
                c.put("age", r.nextInt(60) + 10);
                c.put("address", "中华人民共和国浙江省杭州市大帝国2333号");
                c.put("phone", generatePhone());
                c.put("createTime", new Date(time - r.nextInt(1000) * 1000 * 3600L * 24 + r.nextInt(100000)));
                citizenList.add(c);
            }
            commonMapper.batchInsertCitizen(citizenList);
        }
    }

    @Test
    public void initDataDetail() {
        List<Map<String, Object>> citizenList = commonMapper.selectCtizenList(10000);
        Random r = new Random();
        List<Map<String, Object>> citizenDetailList = new ArrayList<>();
        for (int i = 0; i < citizenList.size(); i++) {
            Map<String, Object> citizen = citizenList.get(i);
            Map<String, Object> detail = Maps.newHashMap();
            detail.put("idCard", citizen.get("id_card"));
            detail.put("account", citizen.get("account"));
            detail.put("detail", detailArr[r.nextInt(detailArr.length)]);
            citizenDetailList.add(detail);
        }
        commonMapper.batchInsertCitizenDetail(citizenDetailList);
    }

    private String generateIdCard() {
        // 330227 19870909 1234
        int one = (int) ((Math.random() * 9 + 1) * 100000);
        int two = (int) ((Math.random() + 1) * 10000000);
        int three = (int) ((Math.random() * 9 + 1) * 1000);
        return new StringBuilder().append(one).append(two).append(three).toString();
    }

    private String generatePhone() {
        long phone = (long) ((Math.random() + 1) * 100000000000L);
        return new StringBuilder().append(phone).toString();
    }

}
