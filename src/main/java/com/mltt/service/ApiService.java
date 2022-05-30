package com.mltt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mltt.biz.model.FUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiService extends IService<FUser> {
    String hello();

    void doTaskOne() throws InterruptedException;

    IPage<FUser> getUser();
}
