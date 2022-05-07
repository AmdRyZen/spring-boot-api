package com.mltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mltt.biz.model.FUser;
import com.mltt.biz.vo.FUserVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiService extends IService<FUser> {
    String hello();

    List<FUserVo> getUser();
}
