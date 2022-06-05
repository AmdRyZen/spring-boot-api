package com.mltt.service.impl;

import com.mltt.biz.model.FUser;
import com.mltt.exception.ServiceException;
import com.mltt.mapper.FUserMapper;
import com.mltt.service.DubboApiService;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@DubboService(version = "1.0", group = "dubboApi", interfaceClass = DubboApiService.class)
public class DubboApiServiceImpl implements DubboApiService {
    private static final Logger log = LoggerFactory.getLogger(DubboApiServiceImpl.class);
    @Resource
    FUserMapper fUserMapper;
    @Override
    public FUser getFuserList() {
        log.info("getFuserList");
        FUser fUserVo = fUserMapper.selectById(1L);
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
