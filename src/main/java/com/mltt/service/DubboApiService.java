package com.mltt.service;

import com.mltt.biz.model.FUser;
import org.springframework.stereotype.Service;

@Service
public interface DubboApiService {
    FUser getFuserList();
}
