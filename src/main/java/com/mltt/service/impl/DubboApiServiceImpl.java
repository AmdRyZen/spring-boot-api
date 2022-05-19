package com.mltt.service.impl;

import com.mltt.service.DubboApiService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0", group = "dubboApi", interfaceClass = DubboApiService.class)
public class DubboApiServiceImpl implements DubboApiService {
    @Override
    public String hello() {
        return "Dubbo  rpc";
    }

}
