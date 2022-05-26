package com.mltt.Component;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RocketMQMessageListener(topic = "spring-sync-tags",consumerGroup = "spring-boot-api")
public class ConsumerListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println("RocketMQMessageListener = " + message);
    }
}
