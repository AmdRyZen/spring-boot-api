package com.mltt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mltt.biz.model.FUser;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Service
public interface ApiService extends IService<FUser> {
    String hello();

    void doTaskOne() throws InterruptedException;

    ListenableFuture<String> async(String message) throws ExecutionException;

    IPage<FUser> getUser();
}
