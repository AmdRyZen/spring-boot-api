package com.mltt.service.impl;

import com.mltt.biz.model.FUser;
import com.mltt.mapper.FUserMapper;
import com.mltt.service.FeignApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class FeignApiServiceImpl implements FeignApiService {
    private static final Logger log = LoggerFactory.getLogger(FeignApiServiceImpl.class);
    @Resource
    FUserMapper fUserMapper;
    @Override
    public String getFuserList() {
        log.info("getFuserList");
        //FUser fUserVo = fUserMapper.selectById(1L);
        return "getFuserList";
    }


}
