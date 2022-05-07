package com.mltt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mltt.biz.model.FUser;
import com.mltt.biz.vo.FUserVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FUserMapper extends BaseMapper<FUser> {
    List<FUserVo> getUser();
}
