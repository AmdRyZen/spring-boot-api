package com.mltt.Component;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "spring-sync-tags",consumerGroup = "spring-producer-group")
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {

        System.out.println("RocketMQMessageListener = " + message);
    }
}
