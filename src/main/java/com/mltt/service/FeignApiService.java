package com.mltt.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@FeignClient
@Service
public interface FeignApiService {
    String getFuserList();
}
