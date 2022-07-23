package com.mltt.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.mltt.biz.dto.DubboDto;
import com.mltt.biz.model.FUser;
import com.mltt.exception.ServiceException;
import com.mltt.mapper.FUserMapper;
import com.mltt.service.DubboApiService;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@DubboService(version = "1.0", group = "dubboApi", interfaceClass = DubboApiService.class)
public class DubboApiServiceImpl implements DubboApiService {
    private static final Logger log = LoggerFactory.getLogger(DubboApiServiceImpl.class);

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    Cache<String, Object> caffeineCache;

    @Resource
    FUserMapper fUserMapper;
    @Override
    public FUser getFuserList(DubboDto dubboDto) {
       /* FUser fUserVo = fUserMapper.selectById(1L);*/
        log.info("traceId = {}, getFuserList", dubboDto.getTraceId());
        // 先从缓存读取
        caffeineCache.getIfPresent(1L);
        FUser fUserVo = (FUser) caffeineCache.asMap().get(String.valueOf(1L));
        if (fUserVo != null){
            log.info("get caffeineCache");
            return fUserVo;
        }
        // 如果缓存中不存在，则从库中查找
        log.info("read Mysql");
        fUserVo = fUserMapper.selectById(1L);
        // 如果用户信息不为空，则加入缓存
        if (fUserVo != null){
            caffeineCache.put(String.valueOf(fUserVo.getId()),fUserVo);
        }
        redisTemplate.opsForValue().set("getFuserList", JSON.toJSONString(fUserVo));

        String jsonValue = redisTemplate.opsForValue().get("getFuserList").toString();
        System.out.println("JSON.parseObject(jsonValue, FUser.class) = " + JSON.parseObject(jsonValue, FUser.class));
        return fUserVo;
    }

    @Override
    public StreamObserver<String> sayHelloStream(StreamObserver<String> response) {
       return new StreamObserver<String>() {
           @Override
           public void onNext(String data) {
               System.out.println(data);
               log.info(data);
               response.onNext("hello,"+data);
           }

           @Override
           public void onError(Throwable throwable) {
               throw new ServiceException(400, throwable.getMessage());
           }

           @Override
           public void onCompleted() {
               System.out.println("onCompleted");
               response.onCompleted();
           }
       };
    }

    @Override
    public void sayHelloServerStream(String request, StreamObserver<String> response) {
        for (int i = 0; i < 10; i++) {
            response.onNext("hello," + request);
        }
        response.onCompleted();
    }

}
