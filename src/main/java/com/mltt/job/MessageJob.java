package com.mltt.job;

import com.mltt.webSocket.WsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MessageJob {

    @Resource
    WsService wsService;

    /**
     * 每20s发送
     */
    @Scheduled(cron = "0/20 * * * * *")
    public void run(){
        try {
            wsService.broadcastMsg("自动生成消息 "  + LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


