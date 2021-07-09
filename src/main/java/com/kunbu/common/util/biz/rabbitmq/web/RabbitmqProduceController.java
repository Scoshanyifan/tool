package com.kunbu.common.util.biz.rabbitmq.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.kunbu.common.util.biz.rabbitmq.mq.RabbitmqConstant;
import com.kunbu.common.util.biz.rabbitmq.service.RabbitmqEventService;
import com.kunbu.common.util.biz.rabbitmq.bean.RabbitmqDto;
import com.kunbu.common.util.web.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqProduceController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitmqEventService rabbitmqEventService;

    @GetMapping("/send")
    public ApiResult send() {
        /**
         * {
         *      "vehicleColor":"Silver",
         *      "passId":7,
         *      "serverAreaId":3,
         *      "plateColor":"Blue",
         *      "vehicleNo":"浙J0S7U9",
         *      "createTime":1623226651000,
         *      "correlationId":"425e7017d793446894183ac400a78743",
         *      "vehicleType":1,
         *      "inOutFlag":"out"
         * }
         */
        RabbitmqDto rabbitmqDto = new RabbitmqDto();
        rabbitmqDto.setCreateTime(LocalDateTime.now());
        rabbitmqDto.setInOutFlag("out");
        rabbitmqDto.setCorrelationId(IdUtil.randomUUID());
        rabbitmqDto.setPassId(7);
        rabbitmqDto.setPlateColor("Blue");
        rabbitmqDto.setServerAreaId(3);
        rabbitmqDto.setVehicleNo("浙J0S7U9");
        rabbitmqDto.setVehicleType(1);
        rabbitmqDto.setVehicleColor("Silver");

        rabbitmqEventService.saveEvent(JSONUtil.toJsonStr(rabbitmqDto), rabbitmqDto.getCorrelationId());
        rabbitTemplate.convertAndSend(
                RabbitmqConstant.EXCHANGE,
                RabbitmqConstant.ROUTING_KEY_VEHICLE,
                JSONUtil.toJsonStr(rabbitmqDto),
                message -> message,
                new CorrelationData(rabbitmqDto.getCorrelationId())
        );
        return ApiResult.success();
    }

    @GetMapping("/send2")
    public ApiResult send2() {

        RabbitmqDto rabbitmqDto = new RabbitmqDto();
        rabbitmqDto.setCreateTime(LocalDateTime.now());
        rabbitmqDto.setInOutFlag("in");
        rabbitmqDto.setCorrelationId(IdUtil.fastSimpleUUID());
        rabbitmqDto.setPassId(2);
        rabbitmqDto.setPlateColor("Yellow");
        rabbitmqDto.setServerAreaId(1);
        rabbitmqDto.setVehicleNo("浙B017U9");
        rabbitmqDto.setVehicleType(2);
        rabbitmqDto.setVehicleColor("Black");

        rabbitmqEventService.saveEvent(JSONUtil.toJsonStr(rabbitmqDto), rabbitmqDto.getCorrelationId());
        rabbitTemplate.convertAndSend(
                RabbitmqConstant.EXCHANGE,
                RabbitmqConstant.ROUTING_KEY_CAR,
                JSONUtil.toJsonStr(rabbitmqDto),
                message -> message,
                new CorrelationData(rabbitmqDto.getCorrelationId())
        );
        return ApiResult.success();
    }

    /**
     * http://localhost:9998/rabbitmq/test?data=qwer&exchange=none-exchange&routingKey=none-topic
     * http://localhost:9998/rabbitmq/test?data=qwer&exchange=exchange-test&routingKey=none-topic
     *
     * @param data
     * @param exchange
     * @param routingKey
     * @return
     */
    @GetMapping("/test")
    public ApiResult test(@RequestParam String data, @RequestParam String exchange, @RequestParam String routingKey) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("messageId", IdUtil.fastSimpleUUID());
        map.put("messageData", data);
        map.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                JSONUtil.toJsonStr(map)
        );
        return ApiResult.success();
    }

    @PostMapping("/save")
    public ApiResult save(@RequestBody RabbitmqDto dto) {
        log.info(">>> dto:{}", dto);
        log.info("进场:{}",JSON.toJSONString(dto));
        return ApiResult.success();
    }
}
