package com.mltt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mltt.biz.model.FUser;
import com.mltt.exception.ServiceException;
import com.mltt.mapper.FUserMapper;
import com.mltt.service.ApiService;
import com.mltt.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class ApiServiceImpl extends ServiceImpl<FUserMapper, FUser> implements ApiService {
    @Resource
    FUserMapper fUserMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public String hello() {
        return StringUtils.filterEmoji("你好中国🇨🇳");
    }

    @Async("threadPoolTaskScheduler")
    public void doTaskOne() throws ServiceException, InterruptedException {
        Thread.sleep(1000);
        System.out.println("开始作任务...");
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor={ServiceException.class})
    public IPage<FUser> getUser() {
        String  value = redisTemplate.opsForValue().get("aaa").toString();
        System.out.println("value = " + value);


        FUser fUserVo = fUserMapper.selectById(1L);
        System.out.println("fUserVo.toString() = " + fUserVo.toString());

        FUser fUser = FUser.builder().username("aaa").avatar("xxxix").last_login_ip("127.0.0.1").phone("111111111").password("qfwqfqwfqw313").salt("1234").build();
        //Optional.of(fUserMapper.insert(fUser)).filter(c -> c.equals(1)).ifPresent(c -> System.out.println("c = " + c));
        //fUserMapper.insert(fUser);
        QueryWrapper<FUser> queryWrapper =  new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<FUser> page = new Page<>(1,10);  // 查询第1页，每页返回5条
        IPage<FUser> iPage = fUserMapper.selectPage(page,queryWrapper);
        return iPage;
    }
}
