package com.mltt;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mltt.mapper")
@EnableDubbo
@NacosConfigurationProperties(dataId = "spring-boot-api-dev.yml", autoRefreshed = true)
@SpringBootApplication
public class MlttApplication {
	public static void main(String[] args) {
		SpringApplication.run(MlttApplication.class, args);
	}
}
