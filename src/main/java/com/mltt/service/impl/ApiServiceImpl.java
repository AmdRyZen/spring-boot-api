package com.mltt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mltt.biz.model.FUser;
import com.mltt.biz.vo.FUserVo;
import com.mltt.exception.ServiceException;
import com.mltt.mapper.FUserMapper;
import com.mltt.service.ApiService;
import com.mltt.utils.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class ApiServiceImpl extends ServiceImpl<FUserMapper, FUser> implements ApiService {
    @Resource
    FUserMapper fUserMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public String hello() {
        return StringUtils.filterEmoji("你好中国🇨🇳");
    }

    @Override
    @Transactional(rollbackFor={ServiceException.class})
    public List<FUserVo> getUser() {
        String  value = redisTemplate.opsForValue().get("aaa").toString();
        System.out.println("value = " + value);


        FUser fUserVo = fUserMapper.selectById(1L);
        System.out.println("fUserVo.toString() = " + fUserVo.toString());

        FUser fUser = FUser.builder().username("aaa").avatar("xxxix").last_login_ip("127.0.0.1").phone("111111111").password("qfwqfqwfqw313").salt("1234").build();
        Optional.of(fUserMapper.insert(fUser)).filter(c -> c.equals(1)).ifPresent(c -> System.out.println("c = " + c));
        //fUserMapper.insert(fUser);

        return fUserMapper.getUser();
    }
}
