package com.mltt.service;

import com.mltt.biz.model.FUser;
import org.apache.dubbo.common.stream.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public interface DubboApiService {
    FUser getFuserList();

    StreamObserver<String> sayHelloStream(StreamObserver<String> response);

    void sayHelloServerStream(String request, StreamObserver<String> response);
}
