package com.mltt.Component;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "transTopic", selectorExpression = "transTag",consumerGroup = "spring-producer-group")
public class CCBTransactionConsumerListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        // 幂等性判断
        // 1、使用唯一字段进行判断，如订单号
        // 2、新建一张带有唯一性字段的表，辅助判断

        // 执行具体业务

        // if(...) {            //执行失败
        // log.error("【执行失败】转账失败！");
        // } else               //执行成功
        log.info("【执行成功】转账成功！建行账户增加" + message + "元！");
        // }
    }
}
