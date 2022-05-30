package com.mltt.service.impl;

import com.mltt.biz.model.FUser;
import com.mltt.mapper.FUserMapper;
import com.mltt.service.DubboApiService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService(version = "1.0", group = "dubboApi", interfaceClass = DubboApiService.class)
public class DubboApiServiceImpl implements DubboApiService {
    @Resource
    FUserMapper fUserMapper;
    @Override
    public FUser getFuserList() {
        FUser fUserVo = fUserMapper.selectById(1L);
        return fUserVo;
    }

}
