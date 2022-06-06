package com.mltt.feign;

import com.mltt.biz.model.FUser;
import com.mltt.exception.ServiceException;
import com.mltt.service.DubboApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feign/api")
public class FeignController {
    private static final Logger log = LoggerFactory.getLogger(FeignController.class);

    @Resource
    public DubboApiService dubboApiService;


    @RequestMapping("/feign")
    public FUser feign() throws ServiceException {
        log.info("boot-api-feign");
        return dubboApiService.getFuserList();
    }
}
