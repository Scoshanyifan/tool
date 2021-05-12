package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.sql.mysql.mybatis.CitizenBean;
import com.kunbu.common.util.tool.sql.mysql.mybatis.biz.CitizenMapper;
import com.kunbu.common.util.web.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author kunbu
 * @date 2021/5/11 17:58
 **/
@RestController
@RequestMapping("/atomic")
public class ConcurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyController.class);

    @Autowired
    private CitizenMapper commonMapper;

    @PostMapping("/insert")
    public ApiResult testBatchInsert(@RequestBody Map<String, String> params) {

        CitizenBean citizenBean = new CitizenBean();
        citizenBean.setAccount(params.get("account"));
        citizenBean.setIdCard(params.get("idCard"));
        fillUpBean(citizenBean);
        citizenBean.setName(params.get("name"));

        int res = -1;
        switch (params.get("type")) {
            case "0":
                int count = commonMapper.countByCondition(citizenBean.getIdCard(), citizenBean.getAccount());
                if (count > 0) {
                    return ApiResult.fail("insert failure");
                }
                res = commonMapper.insertOne(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                break;
            case "1":
                res = commonMapper.insertByNotExistsNormalField(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                break;
            case "2":
                res = commonMapper.insertByNotExistsIndexField(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                break;
            case "3":
                res = commonMapper.insertByCheckNormalField(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                break;
            case "4":
                res = commonMapper.insertByCheckIndexField(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                break;
            case "5":
                res = commonMapper.insertByCheckCount(citizenBean.getIdCard(), citizenBean.getCity(), citizenBean.getAccount(), citizenBean.getName(),
                        citizenBean.getAge(), citizenBean.getAddress(), citizenBean.getPhone(), citizenBean.getCreateTime());
                break;
            default:
                break;
        }

        return ApiResult.success("insert res: " + res);
    }

    private static final String[] ADDRESS_ARR = {"杭州", "大连", "呼和浩特", "乌鲁木齐", "漠河", "广州", "成都", "重庆", "连云港", "上海"};
    private static final String[] NAME_ARR = {"赵四", "钱五", "张三", "孙树", "胡来", "蒋户", "山本", "中粗腾左", "哈哈哈", "撒旦撒"};

    private void fillUpBean(CitizenBean citizenBean) {
//        citizenBean.setAccount("account");
//        citizenBean.setIdCard("idcard");
        Random random = new Random();
        citizenBean.setAddress(ADDRESS_ARR[random.nextInt(9)]);
        citizenBean.setAge(random.nextInt(60) + 10);
        citizenBean.setCity(ADDRESS_ARR[random.nextInt(9)]);
        citizenBean.setCreateTime(new Date());
        citizenBean.setName(NAME_ARR[random.nextInt(9)]);
        citizenBean.setPhone(Integer.toString(random.nextInt(10000)));
    }

}
