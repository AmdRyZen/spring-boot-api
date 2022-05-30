package com.mltt.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.mltt.Annotation.CheckLoginAnnotation;
import com.mltt.biz.dto.ApiDto;
import com.mltt.exception.ServiceException;
import com.mltt.service.ApiService;
import com.mltt.service.DubboApiService;
import com.mltt.utils.ApiResultUtils;
import com.mltt.utils.HttpClientUtils;
import com.mltt.utils.SecurityUtil;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    ApiService apiService;
    @Resource
    JdbcTemplate jdbcTemplate;
    @NacosValue(value = "${username.aa:none}", autoRefreshed = true)
    private String username;

    @RequestMapping("/config")
    public String config() {
        return this.username;
    }

    @DubboReference(version = "1.0", group = "dubboApi", interfaceClass = DubboApiService.class)
    public DubboApiService dubboApiService;

    @CheckLoginAnnotation(desc = "@Annotation")
    @RequestMapping("/hello")
    public String hello() throws InterruptedException {
        apiService.doTaskOne();
        return apiService.hello();
    }

    @RequestMapping("/dubbo")
    public String dubbo() {
        return dubboApiService.hello();
    }

    @RequestMapping("/port")
    public ApiResultUtils port(@RequestBody ApiDto apiDto) throws ServiceException {
        if (apiDto.getPort() != 100) {
            throw new ServiceException(400, "我屮艸芔茻");
        }
        return ApiResultUtils.success(apiDto);
    }

    @RequestMapping("/queryJdbc")
    public ApiResultUtils query(@RequestBody ApiDto apiDto) throws ServiceException {
        return ApiResultUtils.success(jdbcTemplate.queryForList("select * from f_user limit 5"));
    }

    @RequestMapping("/queryMybatis")
    public ApiResultUtils queryMybatis() throws ServiceException {
        return ApiResultUtils.success(apiService.getUser());
    }

    @RequestMapping("/curlPost")
    public ApiResultUtils curlPost() throws ServiceException {
        String result = null;
        try {
            //api url地址
            String url = "xxxxxxxx";
            String param = "cccccccc";
            String aes128Key = "vvvvvvvv";
            String sign = "zzzzzzzzzz";

            Map<String, String> map = new HashMap<>();
            map.put("channelId", "111111");
            map.put("timestamp", "1638263374");
            map.put("param", SecurityUtil.aesEncryptKey16(param, aes128Key));
            map.put("sign", SecurityUtil.md5(sign));
            JSONObject params = new JSONObject(map);
            System.out.println("发送数据：" + params);
            //发送http请求并返回结果
            result = HttpClientUtils.post(url, params.toString());
            System.out.println("java-demo:接收反馈result = " + result);
        } catch (Exception e) {
            System.out.println("------------- " + this.getClass().toString() + ".PostData() : 出现异常 Exception -------------");
            System.out.println(e.getMessage());
        }

        return ApiResultUtils.success(result);
    }


}
