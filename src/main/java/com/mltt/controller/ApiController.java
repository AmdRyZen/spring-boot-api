package com.mltt.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.mltt.Annotation.CheckLoginAnnotation;
import com.mltt.Annotation.DynamicDataSource;
import com.mltt.bean.DataSourceNames;
import com.mltt.biz.dto.ApiDto;
import com.mltt.biz.dto.DubboDto;
import com.mltt.biz.model.FUser;
import com.mltt.exception.ServiceException;
import com.mltt.service.ApiService;
import com.mltt.service.DubboApiService;
import com.mltt.utils.ApiResultUtils;
import com.mltt.utils.HttpClientUtils;
import com.mltt.utils.SecurityUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    @Resource
    ApiService apiService;
    @Resource
    JdbcTemplate jdbcTemplate;
    @NacosValue(value = "${username.aa:none}", autoRefreshed = true)
    private String username;

    @Resource
    private DataSource dataSource;

    @RequestMapping("/fastjson2")
    public String fastjson2() {
        ApiDto apiDto = new ApiDto();
        apiDto.setPort(1);
        apiDto.setName("a");
        for (int i = 0; i < 1000; i++) {
            JSON.toJSONString(apiDto);
        }
        long startTime = System.nanoTime(); //获取开始时间
        String data = JSON.toJSONString(apiDto);
        long endTime = System.nanoTime(); //获取结束时间
        System.out.println("程序运行时间： " +(endTime-startTime)+ "ns");
        return data;
    }

    @RequestMapping("/config")
    @DynamicDataSource(name = DataSourceNames.MASTER)
    public String config() {
        System.out.println(dataSource.getClass().getName());
        return this.username;
    }

    @DubboReference(version = "1.0", group = "dubboApi", interfaceClass = DubboApiService.class)
    public DubboApiService dubboApiService;

    @RequestMapping("/async")
    public ApiResultUtils async() throws ServiceException, ExecutionException, InterruptedException {
        ListenableFuture<String> result = apiService.async("xxx");
        return ApiResultUtils.success(result.get());
    }

    @CheckLoginAnnotation(desc = "@Annotation")
    @RequestMapping("/hello")
    public String hello() throws InterruptedException {
        apiService.doTaskOne();
        return apiService.hello();
    }

    @RequestMapping("/dubbo")
    @DynamicDataSource(name = DataSourceNames.SLAVE)
    public FUser dubbo() {
        DubboDto dto = new DubboDto();
        return dubboApiService.getFuserList(dto);
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
