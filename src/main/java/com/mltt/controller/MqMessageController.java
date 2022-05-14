package com.mltt.controller;

import com.alibaba.fastjson.JSONObject;
import com.mltt.exception.ServiceException;
import com.mltt.utils.ApiResultUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/mqMessage")
public class MqMessageController {
    private static final Logger log = LoggerFactory.getLogger(MqMessageController.class);

    @Resource
    RocketMQTemplate rocketMQTemplate;

    @Value(value = "${rocketmq.producer.sync-tag}")
    private String syncTag;

    @Value(value = "${rocketmq.producer.async-tag}")
    private String asyncTag;

    @Value(value = "${rocketmq.producer.oneway-tag")
    private String onewayTag;


    @RequestMapping("/pushMessage")
    public ApiResultUtils pushMessage() throws ServiceException {
        Long id = 100L;
        System.out.println("syncTag = " + syncTag);
        // 构建消息
        String messageStr = "order id : " + id;
        System.out.println("messageStr = " + messageStr);
       rocketMQTemplate.convertAndSend(syncTag, messageStr);
       log.info("pushMessage finish : " + id + ", sendResult : " + JSONObject.toJSONString(messageStr));

        return ApiResultUtils.success(1L);
    }


}
