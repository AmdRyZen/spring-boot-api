package com.mltt.controller;

import com.mltt.exception.ServiceException;
import com.mltt.utils.ApiResultUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
        System.out.println("syncTag = " + syncTag);
        // 构建消息
        String messageStr = "send sync message";
        System.out.println("messageStr = " + messageStr);
        rocketMQTemplate.convertAndSend(syncTag, messageStr);

        return ApiResultUtils.success(1L);
    }


    @RequestMapping("/pushAsyncMessage")
    public ApiResultUtils pushAsyncMessage() throws ServiceException {
        System.out.println("syncTag = " + syncTag);
        rocketMQTemplate.asyncSend(syncTag, "send async message!", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("send async successful");
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("send fail; {}", throwable.getMessage());
            }
        });

        return ApiResultUtils.success(2L);
    }


}
