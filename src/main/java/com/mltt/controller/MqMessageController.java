package com.mltt.controller;

import com.mltt.exception.ServiceException;
import com.mltt.utils.ApiResultUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

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
        rocketMQTemplate.asyncSend(syncTag, MessageBuilder.withPayload("hello, 这是延迟消息").build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("send async successful");
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("send fail; {}", throwable.getMessage());
            }
        }, 3000, 3);

        return ApiResultUtils.success(2L);
    }

    // TOPIC名称
    private static final String TOPIC = "transTopic";
    // TAG信息
    private static final String TAG = "transTag";

    @RequestMapping("/pushTransactionMessage")
    public ApiResultUtils pushTransactionMessage() throws ServiceException {
        String msg = "1000";
        String transactionId = UUID.randomUUID().toString().replace("-","");
        log.info("【发送半消息】transactionId={}", transactionId);
        String transKeys = "transKey";

        // 发送事务消息
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                TOPIC + ":" + TAG,
                MessageBuilder.withPayload(msg)
                        .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                        .setHeader(RocketMQHeaders.KEYS,transKeys)     // 相比于使用"KEYS"，使用封装常量更不易出错
                        .build(),
                msg
        );
        log.info("【发送半消息】sendResult={}",msg);

        return ApiResultUtils.success(sendResult);
    }


}
